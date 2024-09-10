package com.yaroslav.dragontmsbackend.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Component
@Slf4j
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    // Внедрение через конструктор
    public JwtChannelInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }



    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String destination = accessor.getDestination();

        // Пропускаем аутентификацию для определённых путей
        if (destination != null && destination.startsWith("/ws/**")) {
            log.debug("WebSocket connection to {} does not require authentication", destination);
            return message;  // Пропускаем без проверки токена
        }
        // Получаем токен из заголовков
        List<String> authorizationHeaders = accessor.getNativeHeader("Authorization");
        if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
            String token = authorizationHeaders.get(0).substring(7);  // Убираем "Bearer "

            // Логируем полученный токен
            log.debug("Received WebSocket token: {}", token);

//            // Валидация токена и создание аутентификации
//            UsernamePasswordAuthenticationToken authentication = jwtTokenProvider.validateToken(token);
//            if (authentication != null) {
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }

            try {
                // Выполняем валидацию токена
                UsernamePasswordAuthenticationToken authentication = jwtTokenProvider.validateToken(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new AccessDeniedException("Invalid JWT token");
                }
            } catch (Exception e) {
                log.error("Invalid JWT token: {}", e.getMessage());
                throw new AccessDeniedException("Invalid JWT token: " + e.getMessage());
            }
        } else {
            log.warn("Missing Authorization header in WebSocket message");
            throw new AccessDeniedException("Missing Authorization header");
        }

        return message;
    }
}
