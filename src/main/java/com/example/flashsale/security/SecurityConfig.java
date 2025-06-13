package com.example.flashsale.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 關掉 CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 所有 API 不需認證
                 )
                .httpBasic(httpBasic -> httpBasic.disable()); // 關掉 Basic Auth
        return http.build();
    }
}
