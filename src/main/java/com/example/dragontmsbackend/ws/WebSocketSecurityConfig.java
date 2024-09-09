package com.example.dragontmsbackend.ws;

import com.example.dragontmsbackend.config.JwtChannelInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketSecurityConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    private final JwtChannelInterceptor jwtChannelInterceptor;

    public WebSocketSecurityConfig(JwtChannelInterceptor jwtChannelInterceptor) {
        this.jwtChannelInterceptor = jwtChannelInterceptor;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");  // Простой брокер сообщений
        config.setApplicationDestinationPrefixes("/app");  // Префикс для обращения к контроллерам
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")  // Точка подключения WebSocket
                .setAllowedOrigins(this.allowedOrigins)  // Разрешаем все источники, в реальных проектах лучше ограничить
                .withSockJS();  // Включаем поддержку SockJS
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(jwtChannelInterceptor);  // Внедряем наш интерсептор для обработки JWT
        registration.interceptors();  // Внедряем наш интерсептор для обработки JWT
    }
}