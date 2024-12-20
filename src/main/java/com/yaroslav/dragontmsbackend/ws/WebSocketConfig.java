//package com.example.dragontmsbackend.ws;
//
//
//import com.example.dragontmsbackend.jwt.JwtChannelInterceptor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.ChannelRegistration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
//@Configuration
//@EnableWebSocketMessageBroker
//@Slf4j
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//
//    private final JwtChannelInterceptor jwtChannelInterceptor;  // Внедряем интерсептор
//
//    public WebSocketConfig(JwtChannelInterceptor jwtChannelInterceptor) {
//        this.jwtChannelInterceptor = jwtChannelInterceptor;
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config){
//        config.enableSimpleBroker("/topic");
//        config.setApplicationDestinationPrefixes("/app");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry){
//        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
//        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
//    }
//
//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(jwtChannelInterceptor);  // Добавляем интерсептор
//        log.info("INTERCEPTOR WAS ADDED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
//    }
//}
