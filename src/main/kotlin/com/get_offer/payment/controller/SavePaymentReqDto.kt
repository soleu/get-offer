package com.get_offer.payment.controller

import java.math.BigDecimal

data class SavePaymentReqDto(
    val paymentKey: String,
    val orderId: String,
    val amount: BigDecimal,
)