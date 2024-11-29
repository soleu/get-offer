package com.get_offer.chat.controller

data class ChatMessageReqDto(
    val senderId: Long,
    val content: String,
    val type: String = "TEXT",
)