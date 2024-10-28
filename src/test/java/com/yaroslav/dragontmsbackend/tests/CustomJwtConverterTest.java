package com.yaroslav.dragontmsbackend.tests;


import com.yaroslav.dragontmsbackend.config.CustomJwtConverter;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CustomJwtConverterTest {

    @Test
    void convert_ShouldExtractRolesCorrectly() {
        // Создаем тестовый JWT с realm_access.roles
        Map<String, Object> claims = new HashMap<>();
        Map<String, Object> realmAccess = new HashMap<>();
        realmAccess.put("roles", List.of("ROLE_ADMIN", "USER"));
        claims.put("realm_access", realmAccess);

        Jwt jwt = new Jwt("tokenValue", null, null, null, claims);

        CustomJwtConverter converter = new CustomJwtConverter();
        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        assertNotNull(authorities);
        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void convert_ShouldReturnEmptyWhenNoRoles() {
        // Создаем тестовый JWT без realm_access.roles
        Map<String, Object> claims = new HashMap<>();
        Jwt jwt = new Jwt("tokenValue", null, null, null, claims);

        CustomJwtConverter converter = new CustomJwtConverter();
        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }
}
