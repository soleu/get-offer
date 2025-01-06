package com.get_offer.chat.controller

data class ChatMessageRequest(
    val senderId: Long,
    val content: String,
    val type: String = "TEXT",
)