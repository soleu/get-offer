package com.get_offer.payment.controller

data class SavePaymentReqDto(
    val paymentKey: String,
    val orderId: String,
    val amount: Int,
)