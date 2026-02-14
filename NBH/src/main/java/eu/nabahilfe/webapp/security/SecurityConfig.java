package eu.nabahilfe.webapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/css/**", "/js/**").permitAll()

                .requestMatchers("/registration/**").permitAll()


                .requestMatchers("/").permitAll()

                .requestMatchers("/login").permitAll()

                .requestMatchers("/admin/**")
                    .hasRole("ADMIN")

                .requestMatchers("/billing/**")
                    .hasAnyRole("TREASURER", "AUDITOR")

                .requestMatchers("/docs/**")
                    .hasRole("SECRETARY")

                .requestMatchers("/time/**")
                    .hasRole("TIME_KEEPER")

                .anyRequest().authenticated()
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
                .logoutSuccessUrl("/registration/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
