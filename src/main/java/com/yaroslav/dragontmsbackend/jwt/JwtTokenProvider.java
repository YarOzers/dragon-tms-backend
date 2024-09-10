package com.yaroslav.dragontmsbackend.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Component
public class JwtTokenProvider {

//    // Метод для валидации JWT токена
//    public UsernamePasswordAuthenticationToken validateToken(String token) {
//        // Логика валидации токена
//        // Если токен валиден, возвращаем аутентификацию
//        if (isValid(token)) {
//            // Пример создания аутентификации, где getAuthorities() возвращает роли пользователя
//            return new UsernamePasswordAuthenticationToken(getUsername(token), null, getAuthorities(token));
//        } else {
//            return null;
//        }
//    }

//    // Метод для валидации JWT токена
//    public UsernamePasswordAuthenticationToken validateToken(String token) {
//        // Логика валидации токена
//        // Например, просто возвращаем тестовую аутентификацию для целей тестирования
//        if ("valid-token".equals(token)) { // Замените "valid-token" на реальный токен или используйте реальную логику валидации
//            return new UsernamePasswordAuthenticationToken("testUser", null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
//        }
//        return null; // Возвращаем null, если токен невалиден
//    }

    // Метод для валидации JWT токена
    public UsernamePasswordAuthenticationToken validateToken(String token) {
        // Возвращаем тестового пользователя для всех токенов, игнорируя валидацию
        return new UsernamePasswordAuthenticationToken("testUser", null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    private boolean isValid(String token) {
        // Реализация проверки валидности токена
        return true; // Или ваша реальная логика
    }

    private String getUsername(String token) {
        // Извлечение имени пользователя из токена
        return "user"; // Или реальная логика
    }

    private Collection<GrantedAuthority> getAuthorities(String token) {
        // Извлечение ролей пользователя из токена
        return List.of(new SimpleGrantedAuthority("ROLE_USER")); // Или реальная логика
    }
}
