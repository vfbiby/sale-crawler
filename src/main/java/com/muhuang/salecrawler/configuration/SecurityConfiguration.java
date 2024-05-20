package com.muhuang.salecrawler.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(httpSecurityHttpBasicConfigurer ->
                        httpSecurityHttpBasicConfigurer.authenticationEntryPoint(
                                new BasicAuthenticationEntryPoint()
                        ))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth.requestMatchers(HttpMethod.POST, "/api/1.0/login")
                        .authenticated()
                        .anyRequest().permitAll()
                );
        return http.build();
    }

}
