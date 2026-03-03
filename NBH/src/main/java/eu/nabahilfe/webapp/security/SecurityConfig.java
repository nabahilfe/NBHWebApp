package eu.nabahilfe.webapp.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

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
                .loginPage("/registration/login")			// page
                .loginProcessingUrl("/registration/login") 	// form action
                .successHandler((request, response, authentication) -> {
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
                .failureUrl("/registration/login-error")
                .permitAll()
            )


            .logout(logout -> logout
                .logoutUrl("/registration/logout")
                .invalidateHttpSession(true)             // Session vernichten (Standard)
                .deleteCookies("JSESSIONID")             // Session-Cookie löschen
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
}
