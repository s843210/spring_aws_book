package com.example.demo.config.auth;

import com.example.demo.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final CustomOAuth2UserService customOAuth2UserService;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/", "/css/**", "/images/**", "/js/**",
                                                                "/h2-console/**", "/profile", "/index.html",
                                                                "/posts-save.html", "/posts-update.html")
                                                .permitAll()
                                                .requestMatchers("/api/v1/user").permitAll()
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/v1/posts/**")
                                                .permitAll()
                                                .requestMatchers("/api/v1/**").hasRole(Role.USER.name())
                                                .anyRequest().authenticated())
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/"))
                                .oauth2Login(oauth2 -> oauth2
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .userService(customOAuth2UserService)))
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(
                                                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));

                return http.build();
        }
}