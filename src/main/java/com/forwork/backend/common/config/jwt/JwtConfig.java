package com.forwork.backend.common.config.jwt;

import com.forwork.backend.api.member.jwt.filter.JwtAuthenticationProcessingFilter;
import com.forwork.backend.api.member.jwt.service.JwtService;
import com.forwork.backend.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        return new JwtAuthenticationProcessingFilter(jwtService, memberRepository);
    }
}