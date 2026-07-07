/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.security;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, PersistentTokenRepository tokenRepository,
            CustomUserDetailsService userDetailsService,
            LoginRateLimiter loginRateLimiter,
            LoginRateLimitFilter loginRateLimitFilter) throws Exception {

        http
            .addFilterBefore(loginRateLimitFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/").permitAll()
                .requestMatchers("/css/**", "/js/**").permitAll()
                .requestMatchers("/images/**").permitAll()
                .requestMatchers("/icons/**").permitAll()
                .requestMatchers("/static/**").permitAll()
                .requestMatchers("/apple-touch-icon.png").permitAll()
                .requestMatchers("/manifest.webmanifest").permitAll()
                .requestMatchers("/registration/**").permitAll()
                .requestMatchers("/statuscode/**").permitAll()
                .requestMatchers("/home", "/home/**").permitAll()

                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/registration/login")            	// page
                .loginProcessingUrl("/registration/login")     	// form action
                .successHandler((_ /* request */, response, authentication) -> {
                    log.debug("User '{}' logged in successfully. Auth authorities: {}",
                            authentication.getName(),
                            authentication.getAuthorities());
                    if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
                        log.debug("User '{}' Spring Security roles: {}",
                                authentication.getName(),
                                userDetails.getAuthorities());
                    }
                    response.sendRedirect("/");
                })
                .failureHandler((request, response, _ /* exception */) -> {
                    String ip = getClientIp(request);
                    loginRateLimiter.recordFailure(ip);
                    try {
                        var session = request.getSession();
                        session.setAttribute("LAST_USERNAME", request.getParameter("username"));
                        if (loginRateLimiter.isBlocked(ip)) {
                            session.setAttribute("LOGIN_BLOCK_MINUTES", loginRateLimiter.blockedMinutesRemaining(ip));
                            response.sendRedirect("/registration/login?blocked=true");
                            return;
                        }
                    } catch (Exception e) {
                        log.warn("Could not save last username in session: {}", e.getMessage());
                    }
                    response.sendRedirect("/registration/login?error=true");
                })
                .permitAll()
            )

            .rememberMe(remember -> remember
                    .rememberMeParameter("remember-me")
                    .rememberMeCookieName("remember-me")
                    .tokenValiditySeconds(60 * 60 * 24 * 30) // 30 Tage
                    .tokenRepository(tokenRepository)
                    .userDetailsService(userDetailsService)
                )

            .logout(logout -> logout
                .logoutUrl("/registration/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessUrl("/")
                .permitAll()
            )

            .exceptionHandling(ex -> ex.accessDeniedHandler((request, response, accessDeniedException) -> {
                    log.warn("Zugriff verweigert: {} für Request: {} ", accessDeniedException.getMessage(), request.getRequestURI());
                    response.sendRedirect("/statuscode/403");
                })
            );



        return http.build();
    }


    @Bean
    PersistentTokenRepository persistentTokenRepository(
            DataSource dataSource) {

        JdbcTokenRepositoryImpl repository =
                new JdbcTokenRepositoryImpl();

        repository.setDataSource(dataSource);

        return repository;
    }

    private static String getClientIp(jakarta.servlet.http.HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }
}