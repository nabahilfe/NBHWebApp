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
                .requestMatchers("/registration/**").permitAll()
                .requestMatchers("/statuscode/**").permitAll()

                .requestMatchers("/textcontent/**").permitAll()

                .requestMatchers("/home").permitAll()
                .requestMatchers("/home/**").permitAll()

// FIXME: Hier müssen die Rollen entsprechend der Anforderungen angepasst werden.
//                .requestMatchers("/admin/**")
//                    .hasRole("ADMIN")
//
//                .requestMatchers("/billing/**")
//                    .hasAnyRole("TREASURER", "AUDITOR")
//
//                .requestMatchers("/docs/**")
//                    .hasRole("SECRETARY")
//
//                .requestMatchers("/time/**")
//                    .hasRole("TIME_KEEPER")

                .anyRequest().hasRole("USER")
            )

            .formLogin(form -> form
                .loginPage("/registration/login")			// page
                .loginProcessingUrl("/registration/login") 	// form action
                .defaultSuccessUrl("/", true)
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
