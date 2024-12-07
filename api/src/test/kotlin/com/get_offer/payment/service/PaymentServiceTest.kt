package com.get_offer.payment.service

import com.get_offer.auction.domain.AuctionResult
import com.get_offer.auction.domain.AuctionResultRepository
import com.get_offer.auction.domain.AuctionStatus
import com.get_offer.payment.Payment
import com.get_offer.payment.domain.PaymentRepository
import com.get_offer.user.domain.UserRepository
import java.math.BigDecimal
import java.util.*
import org.apache.coyote.BadRequestException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class PaymentServiceTest {
    private lateinit var mockAuctionRepository: AuctionResultRepository
    private lateinit var mockPaymentRepository: PaymentRepository
    private lateinit var mockUserRepository: UserRepository
    private lateinit var mockFrontendClient: FrontendClient
    private lateinit var paymentService: PaymentService

    @BeforeEach
    fun setUp() {
        mockAuctionRepository = mock(AuctionResultRepository::class.java)
        mockPaymentRepository = mock(PaymentRepository::class.java)
        mockUserRepository = mock(UserRepository::class.java)
        mockFrontendClient = mock(FrontendClient::class.java)
        paymentService =
            PaymentService(mockPaymentRepository, mockUserRepository, mockAuctionRepository, mockFrontendClient)
    }

    @Test
    fun savePaymentWithValidData() {
        // given
        val orderId = 1L
        val userId = 2L
        val productId = 1L
        val paymentKey = "paymentKey123"
        val amount = BigDecimal(5000)

        val order = AuctionResult(
            id = orderId,
            finalPrice = amount,
            auctionName = "aucton_1",
            buyerId = userId,
            productId = productId,
            auctionStatus = AuctionStatus.WAIT
        )

        val savePaymentReq = SavePaymentReq(
            userId = userId,
            orderId = orderId.toString(),
            paymentKey = paymentKey,
            amount = amount
        )

        `when`(mockAuctionRepository.findById(orderId)).thenReturn(Optional.of(order))
        `when`(mockPaymentRepository.save(any(Payment::class.java))).thenReturn(
            Payment(
                userId = userId,
                auctionId = orderId.toString(),
                paymentId = paymentKey
            )
        )

        // when
        val result = paymentService.savePayment(savePaymentReq)

        // then
        assertTrue(result)
        order.auctionStatus = AuctionStatus.COMPLETED
    }

    @Test
    fun savePaymentWithInvalidAmount() {
        // given
        val orderId = 1L
        val userId = 2L
        val productId = 1L
        val paymentKey = "paymentKey123"
        val invalidAmount = BigDecimal(3000) // invalid amount
        val amount = BigDecimal(10000)

        val order = AuctionResult(
            id = orderId,
            finalPrice = amount,
            auctionName = "aucton_1",
            buyerId = userId,
            productId = productId,
            auctionStatus = AuctionStatus.WAIT
        )

        val savePaymentReq = SavePaymentReq(
            userId = userId,
            orderId = orderId.toString(),
            paymentKey = paymentKey,
            amount = invalidAmount
        )

        `when`(mockAuctionRepository.findById(orderId)).thenReturn(Optional.of(order))

        // when & then
        val exception = assertThrows(BadRequestException::class.java) {
            paymentService.savePayment(savePaymentReq)
        }

        assertEquals("결제 정보와 최종 가격이 다릅니다", exception.message)
    }
}
