package com.get_offer.chat.controller

import com.get_offer.chat.service.ChatMessageDto
import com.get_offer.chat.service.ChatRoomDto
import com.get_offer.chat.service.ChatRoomService
import com.get_offer.login.AuthenticatedUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chat")
class ChatController(
    private val chatService: ChatRoomService
) {

    /**
     * 채팅방 생성
     */
    @PostMapping("/{productId}/start")
    fun startChat(
        @AuthenticatedUser userId: Long,
        @PathVariable productId: Long,
    ): ResponseEntity<ChatRoomDto> {
        val chatRoom = chatService.startChat(userId, productId)
        return ResponseEntity.ok(chatRoom)
    }

    /**
     * 과거 채팅 기록 가져오기
     */
    @GetMapping("/{roomId}/history")
    fun getChatMessages(@PathVariable roomId: Long): ResponseEntity<List<ChatMessageDto>> {
        val messages = chatService.getMessages(roomId)
        return ResponseEntity.ok(messages)
    }
}