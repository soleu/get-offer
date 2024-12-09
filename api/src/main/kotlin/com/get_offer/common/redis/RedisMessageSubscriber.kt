package com.get_offer.common.redis

import com.fasterxml.jackson.databind.ObjectMapper
import com.get_offer.chat.domain.ChatMessage
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Component

@Component
class RedisMessageSubscriber : MessageListener {

    override fun onMessage(message: Message, pattern: ByteArray?) {
        val chatMessage = ObjectMapper().readValue(message.body, ChatMessage::class.java)
        println("Received Message: $chatMessage")
        // TODO : 추가적으로 메시지를 DB에 저장하는 로직
    }
}
