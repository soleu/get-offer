package com.get_offer.chat.controller

import com.get_offer.chat.domain.ChatMessage
import com.get_offer.chat.domain.ChatMessageRepository
import com.get_offer.chat.domain.ChatRoomRepository
import com.get_offer.common.multipart.ImageService
import java.time.LocalDateTime
import java.util.*
import org.apache.coyote.BadRequestException
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatWebSocketController(
    private val chatMessageRepository: ChatMessageRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val imageService: ImageService,
) {

    @MessageMapping("/chat/{roomId}/send") // 클라이언트가 이 경로로 메시지 전송
    @SendTo("/topic/chat/{roomId}") // 해당 방에 메시지를 브로드캐스트
    fun handleMessage(@DestinationVariable roomId: String, message: ChatMessageRequest): ChatMessageResponse {
        val chatRoom = chatRoomRepository.findById(roomId.toLong())
            .orElseThrow { BadRequestException("채팅방이 존재하지 않습니다.") }

        var imageUrl = ""

        if (message.type == "IMAGE") {
            val decodeBytes = Base64.getDecoder().decode(message.content)
            imageUrl =
                imageService.saveByteImage(decodeBytes, "chat-images/${roomId}-${System.currentTimeMillis()}.png")
        }

        val chatMessage = ChatMessage(
            chatRoomId = chatRoom.id,
            senderId = message.senderId,
            content = if (message.type == "IMAGE") imageUrl else message.content,
            timestamp = LocalDateTime.now(),
            type = message.type
        )
        chatMessageRepository.save(chatMessage)

        return ChatMessageResponse(
            roomId = roomId.toLong(),
            senderId = message.senderId,
            content = if (message.type == "IMAGE") imageUrl else message.content,
            timestamp = chatMessage.timestamp,
            type = chatMessage.type
        )
    }
}
