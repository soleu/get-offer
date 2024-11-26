package com.get_offer.chat.controller

import com.get_offer.chat.domain.ChatMessage
import com.get_offer.chat.domain.ChatMessageRepository
import com.get_offer.chat.domain.ChatRoomRepository
import java.time.LocalDateTime
import org.apache.coyote.BadRequestException
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatWebSocketController(
    private val chatMessageRepository: ChatMessageRepository,
    private val chatRoomRepository: ChatRoomRepository
) {

    @MessageMapping("/chat/{roomId}/send") // 클라이언트가 이 경로로 메시지 전송
    @SendTo("/topic/chat/{roomId}") // 해당 방에 메시지를 브로드캐스트
    fun handleMessage(@DestinationVariable roomId: String, message: ChatMessageReqDto): ChatMessageResDto {
        val chatRoom = chatRoomRepository.findById(roomId.toLong())
            .orElseThrow { BadRequestException("채팅방이 존재하지 않습니다.") }

        val chatMessage = ChatMessage(
            chatRoomId = chatRoom.id,
            senderId = message.senderId,
            content = message.content,
            timestamp = LocalDateTime.now(),
            image = "", // TODO : 나중에 구현
        )
        chatMessageRepository.save(chatMessage)

        return ChatMessageResDto(
            roomId = roomId.toLong(),
            senderId = message.senderId,
            content = message.content,
            timestamp = chatMessage.timestamp
        )
    }
}
