package com.get_offer.batch


import com.get_offer.auction.domain.AuctionResult
import com.get_offer.auction.domain.AuctionResultRepository
import com.get_offer.auction.domain.AuctionStatus
import com.get_offer.auction.domain.BidRepository
import com.get_offer.common.logger
import com.get_offer.product.domain.ProductRepository
import com.get_offer.product.domain.ProductStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.batch.support.transaction.ResourcelessTransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class EndAuctionJobConfiguration(
    private val jobRepository: JobRepository,
    private val bidRepository: BidRepository,
    private val productRepository: ProductRepository,
    private val auctionResultRepository: AuctionResultRepository,
) {
    @Bean
    fun endAuctionJob(): Job {
        logger().info("end Auction 시작")
        return JobBuilder("endAuctionJob", jobRepository)
            .start(updateProductInProgressStatus())
            .build()
    }

    @Bean
    @JobScope
    fun updateProductInProgressStatus(): Step {
        val txManager: PlatformTransactionManager = ResourcelessTransactionManager()
        val tasklet = Tasklet { _, _ ->
            val products = productRepository.findByStatusAndEndDateLessThanEqual(
                ProductStatus.IN_PROGRESS, LocalDateTime.now()
            )

            products.forEach { product ->
                product.status = ProductStatus.COMPLETED

                // 최고 입찰 조회
                val highestBid = bidRepository.findFirstByProductIdOrderByBidPriceDesc(product.id)

                // 최고가가 상품의 현재가와 다를 때
                if (highestBid?.bidPrice != product.currentPrice) {
                    if (highestBid != null) {
                        product.currentPrice = highestBid.bidPrice
                    }
                }
                // AuctionResult 생성 및 저장
                val auctionResult =
                    if (highestBid == null) {
                        // 낙찰가가 존재하지 않을 때
                        AuctionResult(
                            productId = product.id,
                            buyerId = 0L,
                            finalPrice = BigDecimal(0),
                            auctionStatus = AuctionStatus.FAILED,
                            auctionName = "UNSOLD",
                        )
                    } else {
                        AuctionResult(
                            productId = product.id,
                            buyerId = highestBid.bidderId,
                            finalPrice = highestBid.bidPrice,
                            auctionStatus = AuctionStatus.WAIT,
                            auctionName = product.title,
                        )
                    }

                // 데이터 저장
                productRepository.save(product)
                auctionResultRepository.save(auctionResult)
            }
            RepeatStatus.FINISHED
        }
        return StepBuilder("updateProductInProgressStatusStep", jobRepository)
            .tasklet(tasklet, txManager)
            .build()
    }
}