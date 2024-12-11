package com.get_offer.common.redis

import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class RedisMessageSubscriber(
    private val messagingTemplate: SimpMessagingTemplate,
) : MessageListener {

    override fun onMessage(message: Message, pattern: ByteArray?) {
        val chatMessage = String(message.body)
        messagingTemplate.convertAndSend("/topic/group-chat", chatMessage)
    }
}
