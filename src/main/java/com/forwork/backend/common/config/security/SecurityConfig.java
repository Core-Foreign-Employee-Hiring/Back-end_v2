package com.forwork.backend.common.config.security;

import com.forwork.backend.common.config.jwt.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtConfig jwtConfig;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Arrays.asList(
                            "https://www.korfit.co.kr",
                            "https://api.korfit.co.kr",
                            "http://localhost:3000",
                            "https://korfit-xi.vercel.app",
                            "https://korfit.vercel.app"
                    ));
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH","DELETE", "OPTIONS"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Arrays.asList("Authorization", "Authorization-Refresh","Content-Type", "X-Requested-With", "Accept", "Origin"));
                    config.setMaxAge(3600L);
                    config.addExposedHeader("Authorization");
                    config.addExposedHeader("Authorization-Refresh");
                    return config;
                }))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api-doc", "/health", "/v3/api-docs/**",
                                "/swagger-resources/**","/swagger-ui/**",
                                "/h2-console/**"
                        ).permitAll() // 스웨거, H2, healthCheck 허가
                        .requestMatchers(
                                "/api/v2/member/register", "/api/v2/member/login","/api/v2/member/token-reissue","/api/v2/member/verify-email", "/api/v2/member/find-id/send-code", "/api/v2/member/password-reset/verify-code",
                                "/api/v2/member/verification-email-code","/api/v2/member/verify-userid","/api/v2/member/verify-phone", "/api/v2/member/find-id/verify-code", "/api/v2/member/password-reset/modify",
                                "/api/v2/member/verification-phone-code", "/api/v2/member/find-user-id", "/api/v2/member/employer/company-validate", "/api/v2/member/password-reset/send-code"
                        ).permitAll() // 회원가입, 로그인, 토큰 재발급, 이메일 인증, 사업자등록 번호 인증 허가
                        .requestMatchers(
                                HttpMethod.GET, "/api/v2/recruit/{recruit-id}", "/api/v2/recruit",
                                "/api/v2/recruit-review", "/api/v2/recruit-review/{recruit-review-id}", "/api/v2/recruit-review/{recruit-review-id}/comments","/api/v2/recruit-review/total-count"
                        ).permitAll()
                        .requestMatchers("/api/v2/recruit/**").permitAll()
                        .requestMatchers("/api/v1/pass-archives/detail").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/pass-archives", "/api/v1/pass-archives/{pass-archive-id}/reviews").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));

        http.addFilterBefore(jwtConfig.jwtAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }
}