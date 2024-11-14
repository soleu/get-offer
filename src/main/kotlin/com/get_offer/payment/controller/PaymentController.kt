package com.get_offer.payment.controller

import ApiResponse
import com.get_offer.login.AuthenticatedUser
import com.get_offer.payment.service.PaymentService
import com.get_offer.payment.service.SavePaymentReq
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/payments")
class PaymentController(

    private val paymentService: PaymentService,
) {

    /**
     * 결제 승인 -
     * 프론트엔드가 없어 api call
     */
    @PostMapping("/checkout")
    fun checkout(
        @AuthenticatedUser userId: Long,
        @RequestParam orderId: Long,
    ): String {
        return paymentService.checkout(userId, orderId)
    }

    /**
     * 결제 - 토스페이먼츠에서 결제 승인 받은 것을 DB에 저장
     */
    @PostMapping("/save")
    fun savePayment(
        @AuthenticatedUser userId: Long, @RequestBody req: SavePaymentReqDto
    ): ApiResponse<Boolean> {
        return ApiResponse.success(paymentService.savePayment(SavePaymentReq.of(req, userId)))
    }
}