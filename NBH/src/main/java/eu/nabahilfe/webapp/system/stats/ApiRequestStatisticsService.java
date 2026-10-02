package eu.nabahilfe.webapp.system.stats;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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