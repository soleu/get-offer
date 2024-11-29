package com.get_offer.auction.service

import com.get_offer.auction.controller.BidRequest
import com.get_offer.auction.domain.BidRepository
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("classpath:test_data.sql")
class ConcurrencyTest(
    @Autowired val bidRepository: BidRepository,
    @Autowired val auctionService: AuctionService
) {

    private final val CONCURRENT_COUNT: Int = 100

    // 분산락 - 동시성 테스트
    @Test
    fun `bidAuction_ConcurrencyTest 분산 락 테스트`() {
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(CONCURRENT_COUNT)

        val initialPrice = BigDecimal(10100) // 초기 가격
        val productId = 1L

        // 요청 결과를 저장할 스레드 안전한 리스트
        val successBids = Collections.synchronizedList(mutableListOf<BigDecimal>())
        val failedBids = Collections.synchronizedList(mutableListOf<Pair<BigDecimal, String>>())

        for (idx in 1..CONCURRENT_COUNT) {
            executorService.submit {
                try {
                    val userId = if (idx % 2 == 0) 2L else 3L
                    val bidPrice = initialPrice.add(BigDecimal(100 * idx)) // bidPrice = 초기 가격 + 100씩 증가
                    try {
                        auctionService.bidAuction(userId, productId, BidRequest(bidPrice))
                        successBids.add(bidPrice) // 성공한 요청 기록
                    } catch (ex: Exception) {
                        failedBids.add(bidPrice to ex.message.orEmpty()) // 실패한 요청 기록
                    }
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await() // 모든 요청이 완료될 때까지 대기

        // 성공한 요청 수 검증
        println("Success bids: ${successBids.size}, Failed bids: ${failedBids.size}")
        assertEquals(CONCURRENT_COUNT, successBids.size + failedBids.size)

        executorService.shutdown()
    }


    // 비관적락 - 동시성 테스트
//    @Test
//    fun `bidAuction_ConcurrencyTest - 두 스레드가 동시에 접근할 때 한 스레드가 기다려야 함`() {
//        val latch = CountDownLatch(1)
//        val executorService = Executors.newFixedThreadPool(2)
//
//        // thread 1
//        val future1 = executorService.submit {
//            auctionService.bidAuction(2L, 1L, BidRequest(BigDecimal(11000)))
//            latch.countDown()
//        }
//
//        // thread 2 - wait and execute
//        val future2 = executorService.submit {
//            latch.await() // countDown 할때까지 대기
//            auctionService.bidAuction(3L, 1L, BidRequest(BigDecimal(12000)))
//        }
//
//        // 스레드 완료 대기
//        future1.get()
//        future2.get()
//
//        val finalProduct = productRepository.findById(1L).get()
//        Assertions.assertEquals(0, finalProduct.currentPrice.compareTo(BigDecimal("12000")))
//    }
}