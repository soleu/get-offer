package com.get_offer.payment.controller

import java.math.BigDecimal

data class CheckoutReqDto(
    val userId: String,
    val email: String,
    val username: String,
    val phone: String,
    val amount: BigDecimal,
    val orderName: String,
    val orderId: Long,
)