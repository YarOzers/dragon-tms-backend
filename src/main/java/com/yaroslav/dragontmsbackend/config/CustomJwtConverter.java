package com.yaroslav.dragontmsbackend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CustomJwtConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // Извлекаем объект realm_access
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        if (realmAccess == null) {
            log.warn("Claim 'realm_access' отсутствует в JWT-токене.");
            return Collections.emptyList();
        }

        // Извлекаем список ролей из realm_access
        Object rolesObject = realmAccess.get("roles");
        if (!(rolesObject instanceof List)) {
            log.warn("Claim 'realm_access.roles' отсутствует или имеет неверный тип.");
            return Collections.emptyList();
        }

        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) rolesObject;

        if (roles.isEmpty()) {
            log.warn("Список ролей 'realm_access.roles' пуст.");
            return Collections.emptyList();
        }

        // Преобразуем роли в GrantedAuthority, добавляя префикс "ROLE_", если его нет
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> {
                    if (role.startsWith("ROLE_")) {
                        return new SimpleGrantedAuthority(role);
                    } else {
                        return new SimpleGrantedAuthority("ROLE_" + role);
                    }
                })
                .collect(Collectors.toList());

        log.info("Извлеченные роли: {}", authorities);

        return authorities;
    }
}
