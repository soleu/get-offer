package com.get_offer.payment.service

import com.get_offer.payment.controller.SavePaymentReqDto

data class SavePaymentReq(
    val userId: Long,
    val paymentKey: String,
    val orderId: String,
    val amount: Int,
) {
    companion object {
        fun of(req: SavePaymentReqDto, userId: Long): SavePaymentReq {
            return SavePaymentReq(
                userId,
                req.paymentKey,
                req.orderId,
                req.amount,
            )
        }
    }
}