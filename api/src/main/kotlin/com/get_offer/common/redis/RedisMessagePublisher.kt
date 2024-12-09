package com.get_offer.common.redis

import com.get_offer.chat.domain.ChatMessage
import org.springframework.data.redis.core.RedisTemplate

class RedisMessagePublisher(private val redisTemplate: RedisTemplate<String, Any>) {
    fun publish(topic: String, message: ChatMessage) {
        redisTemplate.convertAndSend(topic, message)
    }
}
