package com.get_offer.auction.service

import com.get_offer.auction.controller.BidRequest
import com.get_offer.product.domain.ProductRepository
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("classpath:test_data.sql")
class ConcurrencyTest(
    @Autowired val productRepository: ProductRepository,
    @Autowired val auctionService: AuctionService
) {
    @Test
    fun `bidAuction_ConcurrencyTest - 두 스레드가 동시에 접근할 때 한 스레드가 기다려야 함`() {
        val latch = CountDownLatch(1)
        val executorService = Executors.newFixedThreadPool(2)

        // thread 1
        val future1 = executorService.submit {
            auctionService.bidAuction(2L, 1L, BidRequest(11000))
            latch.countDown()
        }

        // thread 2 - wait and execute
        val future2 = executorService.submit {
            latch.await() // countDown 할때까지 대기
            auctionService.bidAuction(3L, 1L, BidRequest(12000))
        }

        // 스레드 완료 대기
        future1.get()
        future2.get()

        val finalProduct = productRepository.findById(1L).get()
        Assertions.assertEquals(12000, finalProduct.currentPrice)
    }
}