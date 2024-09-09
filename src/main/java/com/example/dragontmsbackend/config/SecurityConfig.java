package com.example.dragontmsbackend.config;//package com.yaroslav.tms.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                // Используем новую декларативную модель для CSRF
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/test-results", "/ws/**")  // Отключаем CSRF для этих путей
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/ws/**", "/api/test-results").permitAll()  // Разрешаем запросы без токенов
                                .anyRequest().authenticated()  // Остальные запросы требуют аутентификации
                )
                // Отключаем проверку токенов для этих путей
                .oauth2ResourceServer(oauth2ResourceServer -> {
                    oauth2ResourceServer.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()));
                    oauth2ResourceServer.authenticationEntryPoint((request, response, authException) -> {
                        String requestURI = request.getRequestURI();
                        // Проверка: если это не запрос на разрешённые пути, то требуем токен
                        if (!requestURI.startsWith("/ws") && !requestURI.startsWith("/api/test-results")) {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        }
                    });
                })
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new CustomJwtConverter()); // Теперь CustomJwtConverter возвращает Collection<GrantedAuthority>
        return converter;
    }
}
