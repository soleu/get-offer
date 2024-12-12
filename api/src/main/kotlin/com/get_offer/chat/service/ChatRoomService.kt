package com.get_offer.chat.service

import com.get_offer.chat.domain.ChatMessage
import com.get_offer.chat.domain.ChatMessageRepository
import com.get_offer.chat.domain.ChatRoom
import com.get_offer.chat.domain.ChatRoomRepository
import com.get_offer.common.exception.ApiException
import com.get_offer.common.exception.ExceptionCode
import com.get_offer.common.redis.RedisMessagePublisher
import com.get_offer.product.domain.Product
import com.get_offer.product.domain.ProductRepository
import java.util.concurrent.CompletableFuture
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val productRepository: ProductRepository,
    private val redisMessagePublisher: RedisMessagePublisher,
) {
    @Transactional
    fun startChat(requesterId: Long, productId: Long): ChatRoomDto {
        // 기존 채팅방 여부 확인
        val existingRoom = chatRoomRepository.findBySenderIdAndProductId(requesterId, productId)
        if (existingRoom != null) return ChatRoomDto(existingRoom.id)

        // 새 채팅방 생성
        val sellerId = getSellerIdFromProductId(productId)

        if (sellerId == requesterId) throw BadRequestException("채팅 상대방이 본인일 수 없습니다.")
        val chatRoom =
            chatRoomRepository.save(ChatRoom(senderId = requesterId, sellerId = sellerId, productId = productId))
        return ChatRoomDto(chatRoom.id)
    }

    fun getMessages(roomId: Long): List<ChatMessageDto> {
        return chatMessageRepository.findByChatRoomId(roomId)
            .map { ChatMessageDto.of(it) }
    }

    // redis message DB에 저장
    fun processMessage(message: ChatMessage) {
        val topic = "/topic/group-chat/${message.chatRoomId}"
        // redis 퍼블리시
        redisMessagePublisher.publish(topic, message)
        println("topic::::: $topic")
        // 비동기로 DB 저장
        CompletableFuture.runAsync {
            chatMessageRepository.save(message)
        }
    }

    @Transactional
    fun createGroupChatRoom(product: Product) {
        // db에 채팅방 생성
        chatRoomRepository.save(ChatRoom(sellerId = product.writerId, productId = product.id))
    }

    private fun getSellerIdFromProductId(productId: Long): Long {
        return productRepository.findById(productId)
            .orElseThrow { ApiException(ExceptionCode.NOTFOUND, "$productId 의 상품은 존재하지 않습니다.") }
            .writerId
    }
}
