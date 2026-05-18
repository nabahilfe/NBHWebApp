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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, PersistentTokenRepository tokenRepository,
            CustomUserDetailsService userDetailsService) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/").permitAll()
                .requestMatchers("/css/**", "/js/**").permitAll()
                .requestMatchers("/images/**").permitAll()
                .requestMatchers("/registration/**").permitAll()
                .requestMatchers("/statuscode/**").permitAll()
                .requestMatchers("/home", "/home/**").permitAll()

                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/registration/login")            // page
                .loginProcessingUrl("/registration/login")     // form action
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
                    // store submitted username temporarily in the session so the login page can repopulate it
                    try {
                        var session = request.getSession();
                        session.setAttribute("LAST_USERNAME", request.getParameter("username"));
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
}