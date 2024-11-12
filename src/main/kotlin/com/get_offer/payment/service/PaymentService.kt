package com.get_offer.payment.service

import com.get_offer.auction.controller.repository.AuctionResultRepository
import com.get_offer.payment.Payment
import com.get_offer.payment.controller.CheckoutRequest
import com.get_offer.payment.controller.FrontendClient
import com.get_offer.payment.domain.PaymentRepository
import com.get_offer.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val userRepository: UserRepository,
    private val auctionRepository: AuctionResultRepository,
    private val frontendClient: FrontendClient,
) {
    fun checkout(
        userId: Long,
        orderId: Long,
    ): String {
        val user = userRepository.findById(userId)
            .orElseThrow()
        val order = auctionRepository.findById(orderId)
            .orElseThrow()

        return frontendClient.checkout(
            CheckoutRequest(
                userId = user.id.toString(),
                email = "",
                username = user.nickname,
                phone = "",
                amount = order.finalPrice,
                orderName = "",
                orderId = order.id,
            )
        )
    }

    fun savePayment(req: SavePaymentReq) {
        val order = auctionRepository.findById(req.orderId.toLong())
            .orElseThrow()

        if (order.finalPrice != req.amount) {
            throw IllegalArgumentException()
        }

        paymentRepository.save(
            Payment(
                userId = req.userId,
                auctionId = req.orderId,
                paymentId = req.paymentKey,
            )
        )
    }
}