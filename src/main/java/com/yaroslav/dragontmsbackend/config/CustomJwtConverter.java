package com.yaroslav.dragontmsbackend.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;

public class CustomJwtConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();

        // Используем стандартный конвертер для извлечения прав доступа
        Collection<GrantedAuthority> authorities = defaultConverter.convert(jwt);

        // Можно добавить дополнительные права (если нужно), или вернуть то, что вернул JwtGrantedAuthoritiesConverter
        return authorities;
    }
}
