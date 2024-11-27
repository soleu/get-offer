package com.get_offer.common.websocket

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic") // 클라이언트 구독 경로
        config.setApplicationDestinationPrefixes("/app") // 서버 메시지 수신 경로
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws") // WebSocket 연결 엔드포인트
            .setAllowedOriginPatterns("*") // CORS 허용
            .withSockJS() // SockJS 지원
    }

    override fun configureWebSocketTransport(registration: WebSocketTransportRegistration) {
        registration.setMessageSizeLimit(1024 * 1024) // 메시지 크기 제한 1MB
        registration.setSendBufferSizeLimit(1024 * 1024) // 전송 버퍼 크기 1MB
        registration.setSendTimeLimit(20_000) // 메시지 전송 제한 시간 20초
    }
}
