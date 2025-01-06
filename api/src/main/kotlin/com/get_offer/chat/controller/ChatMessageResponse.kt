package com.get_offer.chat.controller

import java.time.LocalDateTime

data class ChatMessageResponse(
    val roomId: Long,
    val senderId: Long,
    val content: String,
    val type: String,
    val timestamp: LocalDateTime,
)