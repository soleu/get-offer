package com.get_offer.batch

import com.get_offer.product.domain.Product
import com.get_offer.product.domain.ProductStatus
import jakarta.persistence.EntityManagerFactory
import java.time.LocalDateTime
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.batch.support.transaction.ResourcelessTransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager


@Configuration
class AuctionJobConfiguration(
    private val entityManagerFactory: EntityManagerFactory,
    private val jobRepository: JobRepository,
) {
    @Bean
    fun startAuction(): Job {
        return JobBuilder("a", jobRepository)
            .start(updateProductWaitStatus())
            .build()
    }

    @Bean
    @JobScope
    fun updateProductWaitStatus(): Step {
        val txManager: PlatformTransactionManager = ResourcelessTransactionManager()
        return StepBuilder("findProductWaitStep", jobRepository)
            .chunk<Product, Product>(10, txManager) // 10개씩 처리
            .reader(productItemReader())
            .processor(productItemProcessor())
            .writer(productItemWriter())
            .build()
    }

    @Bean
    @JobScope
    fun productItemReader(): JpaPagingItemReader<Product> {
        return JpaPagingItemReaderBuilder<Product>()
            .name("productItemReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString(
                "UPDATE products" +
                        "SET status = 'IN_PROGRESS'" +
                        "WHERE id IN (" +
                        "    SELECT id FROM products" +
                        "    WHERE status = 'WAIT' AND start_date <= :currentDate" +
                        ");"
            )
            .parameterValues(mapOf("currentDate" to LocalDateTime.now()))
            .pageSize(10)
            .build()
    }

    @Bean
    @JobScope
    fun productItemProcessor(): (Product) -> Product {
        return { product ->
            product.status = ProductStatus.IN_PROGRESS
            product
        }
    }


    @Bean
    @JobScope
    fun productItemWriter(): JpaItemWriter<Product> {
        val writer = JpaItemWriter<Product>()
        writer.setEntityManagerFactory(entityManagerFactory)
        return writer
    }
}