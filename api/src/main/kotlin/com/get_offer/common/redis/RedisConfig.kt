package com.get_offer.common.redis

import com.get_offer.chat.service.ChatRoomService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter

@Configuration
class RedisConfig {

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = redisConnectionFactory
        return template
    }

    @Bean
    fun redisMessageListenerContainer(
        connectionFactory: RedisConnectionFactory,
        listenerAdapter: MessageListenerAdapter
    ): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        container.connectionFactory = connectionFactory
        container.addMessageListener(listenerAdapter, ChannelTopic("group-chat"))
        return container
    }

    @Bean
    fun listenerAdapter(service: ChatRoomService): MessageListenerAdapter {
        return MessageListenerAdapter(service, "processMessage")
    }

    @Bean
    fun channelTopic(): ChannelTopic {
        return ChannelTopic("group-chat")
    }
}
