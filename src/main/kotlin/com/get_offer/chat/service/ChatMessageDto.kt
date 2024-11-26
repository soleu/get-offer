package com.get_offer.chat.service

import com.get_offer.chat.domain.ChatMessage
import java.time.LocalDateTime

data class ChatMessageDto(
    val chatRoomId: Long,
    val senderId: Long,
    val content: String,
    val timestamp: LocalDateTime,
    val image: String,
    val id: Long
) {
    companion object {
        fun of(message: ChatMessage): ChatMessageDto {
            return ChatMessageDto(
                message.chatRoomId,
                message.senderId,
                message.content,
                message.timestamp,
                message.image,
                message.id,
            )
        }
    }
}