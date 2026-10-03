// src/main/java/eu/nabahilfe/webapp/system/stats/ApiRequestStatisticRepository.java
package eu.nabahilfe.webapp.system.stats;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ApiRequestStatisticRepository
                extends JpaRepository<ApiRequestStatistic, Long> {

        List<ApiRequestStatistic> findByRequestDateBetweenOrderByRequestCountDesc(
                        LocalDate fromDate, LocalDate toDate);

        Optional<ApiRequestStatistic> findByRequestDateAndHttpMethodAndUrlPatternAndStatusCode(
                        LocalDate date,
                        String httpMethod,
                        String urlPattern,
                        int statusCode);
}