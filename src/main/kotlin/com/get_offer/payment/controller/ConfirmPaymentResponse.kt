package com.get_offer.payment.controller

data class ConfirmPaymentResponse(
    val paymentKey: String,
    val orderId: String,
    val amount: Int,
    val status: String,
    // 추가적인 응답 필드를 필요에 따라 추가
)