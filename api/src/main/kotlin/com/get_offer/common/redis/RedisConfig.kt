package com.get_offer.common.redis

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.PatternTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer

@Configuration
class RedisConfig {

    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory {
        return LettuceConnectionFactory(RedisStandaloneConfiguration("localhost", 6379))
    }

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = redisConnectionFactory
        template.valueSerializer = GenericJackson2JsonRedisSerializer()
        return template
    }

    @Bean
    fun messageListenerAdapter(): MessageListenerAdapter {
        return MessageListenerAdapter(RedisMessageSubscriber())
    }

    @Bean
    fun redisContainer(
        connectionFactory: RedisConnectionFactory,
        listenerAdapter: MessageListenerAdapter
    ): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        container.connectionFactory = connectionFactory
        container.addMessageListener(listenerAdapter, PatternTopic("chat:*"))
        return container
    }

    @Bean
    fun redisMessagePublisher(redisTemplate: RedisTemplate<String, Any>): RedisMessagePublisher {
        return RedisMessagePublisher(redisTemplate)
    }
}
