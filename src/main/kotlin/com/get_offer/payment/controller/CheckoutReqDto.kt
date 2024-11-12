package com.get_offer.payment.controller

data class CheckoutReqDto(
    val userId: String,
    val email: String,
    val username: String,
    val phone: String,
    val amount: Int,
    val orderName: String,
    val orderId: Long,
)