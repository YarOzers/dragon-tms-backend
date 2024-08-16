package com.example.dragontmsbackend.config;//package com.yaroslav.tms.config;
//
//import com.yaroslav.tms.jwt.CustomJwt;
//import com.yaroslav.tms.jwt.CustomJwtConverter;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@Slf4j
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.cors(Customizer.withDefaults())
//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests
////                                .requestMatchers("/api/*").hasRole("user")
//                                .anyRequest().authenticated()
//                )
//                .oauth2ResourceServer(oauth2ResourceServer ->
//                        oauth2ResourceServer.jwt(jwt -> jwt.jwtAuthenticationConverter(customJwtConverter())
//                ))
//                .sessionManagement(sessionManagement ->
//                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                );
//
//        return http.build();
//    }
//
//    private Converter<Jwt, CustomJwt> customJwtConverter() {
//        return new CustomJwtConverter();
//    }
//
//}
