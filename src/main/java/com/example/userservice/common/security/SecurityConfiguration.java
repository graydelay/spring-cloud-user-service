package com.example.userservice.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@Configuration
public class SecurityConfiguration {

    /**
     * SpringSecurityFilterChain을 설정하는 Bean
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/user-service/welcome")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/user-service/health_check")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/user-service/users/**", "GET")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/user-service/users/**", "POST")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                        .requestMatchers("/**").access(
                                new WebExpressionAuthorizationManager("hasIpAddress('127.0.0.1') or hasIpAddress('172.30.1.48')"))
                        .anyRequest().authenticated()
                )
                        .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.headers((headers) -> headers.frameOptions(frameOption -> frameOption.sameOrigin()));

        return http.build();
    }
}
