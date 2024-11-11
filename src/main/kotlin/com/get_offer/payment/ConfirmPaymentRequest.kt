package com.get_offer.payment

data class ConfirmPaymentRequest(
    val paymentKey: String,
    val orderId: String,
    val amount: Int
)