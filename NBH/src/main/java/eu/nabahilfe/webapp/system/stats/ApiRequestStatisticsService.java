// src/main/java/eu/nabahilfe/webapp/system/stats/ApiRequestStatisticsService.java
package eu.nabahilfe.webapp.system.stats;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiRequestStatisticsService {

    private final ApiRequestStatisticRepository repository;
    private final ApiRequestCounter counter;

    public ApiRequestStatisticsService(
            ApiRequestStatisticRepository repository,
            ApiRequestCounter counter) {
        this.repository = repository;
        this.counter = counter;
    }

    @Transactional(readOnly = true)
    public List<ApiRequestStatistic> findLast7Days() {
        LocalDate today = LocalDate.now();
        return repository.findByRequestDateBetweenOrderByRequestCountDesc(today.minusDays(6), today);
    }

    @Transactional(readOnly = true)
    public List<ApiRequestStatistic> findLast30Days() {
        LocalDate today = LocalDate.now();
        return repository.findByRequestDateBetweenOrderByRequestCountDesc(today.minusDays(29), today);
    }

    @SuppressWarnings("null")
    public List<ApiRequestDailyTotal> dailyTotals(List<ApiRequestStatistic> statistics) {
        LocalDate today = LocalDate.now();
        Map<LocalDate, Long> totals = new HashMap<>();
        for (ApiRequestStatistic statistic : statistics) {
            totals.merge(statistic.getRequestDate(), statistic.getRequestCount(), Long::sum);
        }
        return today.minusDays(29).datesUntil(today.plusDays(1))
                .map(date -> new ApiRequestDailyTotal(date, totals.getOrDefault(date, 0L)))
                .toList();
    }

    @Transactional
    public void flush() {

        Map<ApiRequestStatisticKey, Long> snapshot = counter.drain();

        for (Map.Entry<ApiRequestStatisticKey, Long> entry : snapshot.entrySet()) {

            ApiRequestStatisticKey key = entry.getKey();
            long count = entry.getValue();

            ApiRequestStatistic statistic = repository
                    .findByRequestDateAndHttpMethodAndUrlPatternAndStatusCode(
                            key.date(),
                            key.httpMethod(),
                            key.urlPattern(),
                            key.statusCode())
                    .orElseGet(() -> new ApiRequestStatistic(
                            key.date(),
                            key.httpMethod(),
                            key.urlPattern(),
                            key.statusCode(),
                            0L));

            statistic.addCount(count);
            repository.save(statistic);
        }
    }
}