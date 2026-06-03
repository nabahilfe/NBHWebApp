package eu.nabahilfe.webapp.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.security.CustomUserDetails;

@Configuration(proxyBeanMethods = false)
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

    @Bean
    AuditorAware<Member> auditorProvider() {

        return () -> {

            Authentication auth =
                    SecurityContextHolder.getContext().getAuthentication();

            if (auth == null ||
                !auth.isAuthenticated() ||
                "anonymousUser".equals(auth.getPrincipal())) {

                return Optional.empty();
            }

            Object principal = auth.getPrincipal();

            if (principal instanceof CustomUserDetails userDetails) {
                return Optional.of(userDetails.getMember());
            }

            return Optional.empty();
        };
    }
}