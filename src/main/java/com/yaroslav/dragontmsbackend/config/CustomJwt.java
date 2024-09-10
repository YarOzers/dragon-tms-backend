package com.yaroslav.dragontmsbackend.config;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;


@Getter
public class CustomJwt extends JwtAuthenticationToken {

    private final String username;
    private final Collection<GrantedAuthority> authorities; // Изменяем тип на Collection<GrantedAuthority>

    public CustomJwt(Jwt jwt, String username, Collection<GrantedAuthority> authorities) {
        super(jwt, authorities);
        this.username = username;
        this.authorities = authorities;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }
}