package com.get_offer.payment.controller

data class ConfirmPaymentResDto(
    val paymentKey: String,
    val orderId: String,
    val amount: Int,
    val status: String,
)