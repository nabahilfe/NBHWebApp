package eu.nabahilfe.webapp.system;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ApiRequestStatisticsScheduler {

    private final ApiRequestStatisticsService statisticsService;

    public ApiRequestStatisticsScheduler(ApiRequestStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Scheduled(fixedDelay = 60_000)
    public void flushStatistics() {
        statisticsService.flush();
    }
}