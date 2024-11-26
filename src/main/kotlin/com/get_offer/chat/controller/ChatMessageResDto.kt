package com.get_offer.chat.controller

import java.time.LocalDateTime

data class ChatMessageResDto(
    val roomId: Long,
    val senderId: Long,
    val content: String,
    val timestamp: LocalDateTime,
)