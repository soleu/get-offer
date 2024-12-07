package com.get_offer.batch

import com.get_offer.common.logger
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
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager


@Configuration
class StartAuctionJobConfiguration(
    private val entityManagerFactory: EntityManagerFactory,
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager
) {
    @Bean
    fun startAuctionJob(): Job {
        logger().info("start Auction 시작")
        return JobBuilder("startAuctionJob", jobRepository)
            .start(updateProductWaitStatus())
            .build()
    }

    @Bean
    @JobScope
    fun updateProductWaitStatus(): Step {
        return StepBuilder("findProductWaitStep", jobRepository)
            .chunk<Product, Product>(10, transactionManager)
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
                "SELECT p FROM Product p WHERE p.status = 'WAIT' AND p.startDate <= :currentDate"
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
