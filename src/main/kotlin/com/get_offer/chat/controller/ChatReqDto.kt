package com.get_offer.chat.controller

data class ChatReqDto(
    val sellerName: String,
    val roomId: Long,
    val userId: Long,
)
