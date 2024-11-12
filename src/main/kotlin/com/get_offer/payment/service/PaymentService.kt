package com.get_offer.payment.service

import com.get_offer.auction.controller.repository.AuctionResultRepository
import com.get_offer.common.exception.ApiException
import com.get_offer.common.exception.ExceptionCode
import com.get_offer.payment.Payment
import com.get_offer.payment.controller.CheckoutReqDto
import com.get_offer.payment.domain.PaymentRepository
import com.get_offer.user.domain.UserRepository
import org.apache.coyote.BadRequestException
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
            .orElseThrow { ApiException(ExceptionCode.NOTFOUND, "$userId 의 사용자는 존재하지 않습니다.") }
        val order = auctionRepository.findById(orderId)
            .orElseThrow { ApiException(ExceptionCode.NOTFOUND, "$orderId 의 경매 내역은 존재하지 않습니다.") }

        return frontendClient.checkout(
            CheckoutReqDto(
                userId = user.id.toString(),
                email = user.email,
                username = user.nickname,
                phone = user.phone,
                amount = order.finalPrice,
                orderName = order.auctionName,
                orderId = order.id,
            )
        )
    }

    fun savePayment(req: SavePaymentReq): Boolean {
        val order = auctionRepository.findById(req.orderId.toLong())
            .orElseThrow()

        if (order.finalPrice != req.amount) {
            throw BadRequestException("결제 정보와 최종 가격이 다릅니다")
        }

        paymentRepository.save(
            Payment(
                userId = req.userId,
                auctionId = req.orderId,
                paymentId = req.paymentKey,
            )
        )
        return true
    }
}