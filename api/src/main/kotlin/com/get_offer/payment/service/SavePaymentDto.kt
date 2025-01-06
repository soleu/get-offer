package com.get_offer.payment.service

import com.get_offer.payment.controller.SavePaymentRequest
import java.math.BigDecimal

data class SavePaymentDto(
    val userId: Long,
    val paymentKey: String,
    val orderId: String,
    val amount: BigDecimal,
) {
    companion object {
        fun of(req: SavePaymentRequest, userId: Long): SavePaymentDto {
            return SavePaymentDto(
                userId,
                req.paymentKey,
                req.orderId,
                req.amount,
            )
        }
    }
}