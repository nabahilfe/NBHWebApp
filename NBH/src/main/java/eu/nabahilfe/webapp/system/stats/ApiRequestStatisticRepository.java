package eu.nabahilfe.webapp.system.stats;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ApiRequestStatisticRepository
                extends JpaRepository<ApiRequestStatistic, Long> {

        Optional<ApiRequestStatistic> findByRequestDateAndHttpMethodAndUrlPatternAndStatusCode(
                        LocalDate date,
                        String httpMethod,
                        String urlPattern,
                        int statusCode);
}