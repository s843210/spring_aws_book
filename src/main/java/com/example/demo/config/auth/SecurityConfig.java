package com.example.demo.config.auth;

import com.example.demo.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration // @EnableWebSecurity에 포함되어 있어 생략 가능하지만 명시적으로 사용하는 것을 권장합니다.
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // csrf와 frameOptions 비활성화
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))

                // URL 별 권한 관리 설정
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                        .requestMatchers("/api/v1/**").hasRole(Role.USER.name())
                        .anyRequest().authenticated()
                )

                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                )

                // OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                );

        return http.build();
    }
}