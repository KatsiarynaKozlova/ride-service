package com.software.modsen.rideservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class RideSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .oauth2ResourceServer(oauth
                        -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtAuthenticationConverterRide()))
                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.GET, "/actuator/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                );
        return http.build();
    }
}
