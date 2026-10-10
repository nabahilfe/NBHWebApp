// src/test/java/eu/nabahilfe/webapp/system/stats/ApiRequestStatisticsTest.java
package eu.nabahilfe.webapp.system.stats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.ExtendedModelMap;

import tools.jackson.databind.ObjectMapper;

class ApiRequestStatisticsTest {

    @Test
    void queriesExactly7CalendarDaysIncludingToday() {
        ApiRequestStatisticRepository repository = mock(ApiRequestStatisticRepository.class);
        ApiRequestStatisticsService service = new ApiRequestStatisticsService(repository, mock(ApiRequestCounter.class));
        List<ApiRequestStatistic> statistics = List.of(
                new ApiRequestStatistic(LocalDate.now(), "GET", "/home", 200, 42L));
        when(repository.findByRequestDateBetweenOrderByRequestCountDesc(any(), any())).thenReturn(statistics);

        LocalDate before = LocalDate.now();
        assertSame(statistics, service.findLast7Days());
        LocalDate after = LocalDate.now();

        ArgumentCaptor<LocalDate> fromDate = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> toDate = ArgumentCaptor.forClass(LocalDate.class);
        verify(repository).findByRequestDateBetweenOrderByRequestCountDesc(fromDate.capture(), toDate.capture());
        org.junit.jupiter.api.Assertions.assertTrue(
                !toDate.getValue().isBefore(before) && !toDate.getValue().isAfter(after));
        assertEquals(toDate.getValue().minusDays(6), fromDate.getValue());
    }

    @Test
    void queriesExactly30CalendarDaysIncludingToday() {
        ApiRequestStatisticRepository repository = mock(ApiRequestStatisticRepository.class);
        ApiRequestStatisticsService service = new ApiRequestStatisticsService(repository, mock(ApiRequestCounter.class));
        List<ApiRequestStatistic> statistics = List.of(
                new ApiRequestStatistic(LocalDate.now(), "GET", "/home", 200, 42L));
        when(repository.findByRequestDateBetweenOrderByRequestCountDesc(any(), any())).thenReturn(statistics);

        LocalDate before = LocalDate.now();
        assertSame(statistics, service.findLast30Days());
        LocalDate after = LocalDate.now();

        ArgumentCaptor<LocalDate> fromDate = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> toDate = ArgumentCaptor.forClass(LocalDate.class);
        verify(repository).findByRequestDateBetweenOrderByRequestCountDesc(fromDate.capture(), toDate.capture());
        org.junit.jupiter.api.Assertions.assertTrue(
                !toDate.getValue().isBefore(before) && !toDate.getValue().isAfter(after));
        assertEquals(toDate.getValue().minusDays(29), fromDate.getValue());
    }

    @Test
    void suppliesStatisticsToTheMenuTargetTemplate() {
        ApiRequestStatisticsService service = mock(ApiRequestStatisticsService.class);
        List<ApiRequestStatistic> statistics = List.of();
        LocalDate today = LocalDate.now();
        ApiRequestStatistic nonRootStatistic = new ApiRequestStatistic(today.minusDays(29), "GET", "/home", 200, 42L);
        List<ApiRequestStatistic> chartStatistics = List.of(
            nonRootStatistic,
            new ApiRequestStatistic(today, "GET", "/", 200, 100L),
            new ApiRequestStatistic(today, "POST", "/", 500, 10L));
        ApiRequestStatisticsService aggregationService = new ApiRequestStatisticsService(
            mock(ApiRequestStatisticRepository.class), mock(ApiRequestCounter.class));
        List<ApiRequestDailyTotal> dailyTotals = aggregationService.dailyTotals(chartStatistics);
        List<ApiRequestStatistic> filteredChartStatistics = List.of(nonRootStatistic);
        List<ApiRequestDailyTotal> filteredDailyTotals = aggregationService.dailyTotals(filteredChartStatistics);
        when(service.findLast7Days()).thenReturn(statistics);
        when(service.findLast30Days()).thenReturn(chartStatistics);
        when(service.dailyTotals(chartStatistics)).thenReturn(dailyTotals);
        when(service.dailyTotals(filteredChartStatistics)).thenReturn(filteredDailyTotals);
        ObjectMapper objectMapper = new ObjectMapper();
        ExtendedModelMap model = new ExtendedModelMap();

        assertEquals("system/stats/api-requests", new ApiRequestStatisticsController(service, objectMapper).showApiRequests(model));
        assertSame(statistics, model.getAttribute("statistics"));
        assertEquals(objectMapper.writeValueAsString(dailyTotals), model.getAttribute("dailyTotalsJson"));
        assertEquals(objectMapper.writeValueAsString(filteredDailyTotals), model.getAttribute("filteredDailyTotalsJson"));
        assertEquals(30, filteredDailyTotals.size());
        assertEquals(new ApiRequestDailyTotal(today.minusDays(29), 42L), filteredDailyTotals.getFirst());
        assertEquals(new ApiRequestDailyTotal(today, 110L), dailyTotals.getLast());
        assertEquals(new ApiRequestDailyTotal(today, 0L), filteredDailyTotals.getLast());
        verify(service).findLast7Days();
        verify(service).findLast30Days();
        verify(service).dailyTotals(chartStatistics);
        verify(service).dailyTotals(filteredChartStatistics);
    }

    @Test
    void sumsAllMethodsUrlsAndStatusesPerDayAndFillsMissingDays() {
        ApiRequestStatisticsService service = new ApiRequestStatisticsService(
                mock(ApiRequestStatisticRepository.class), mock(ApiRequestCounter.class));
        LocalDate today = LocalDate.now();
        List<ApiRequestStatistic> statistics = List.of(
                new ApiRequestStatistic(today, "GET", "/home", 200, 42L),
                new ApiRequestStatistic(today, "POST", "/members", 500, 8L),
                new ApiRequestStatistic(today.minusDays(29), "GET", "/home", 200, 12L),
                new ApiRequestStatistic(today.minusDays(30), "GET", "/home", 200, 100L),
                new ApiRequestStatistic(today.plusDays(1), "GET", "/home", 200, 100L));

        List<ApiRequestDailyTotal> totals = service.dailyTotals(statistics);

        assertEquals(30, totals.size());
        assertEquals(new ApiRequestDailyTotal(today.minusDays(29), 12L), totals.getFirst());
        assertEquals(new ApiRequestDailyTotal(today, 50L), totals.getLast());
        for (int index = 1; index < 29; index++) {
            assertEquals(new ApiRequestDailyTotal(today.minusDays(29 - index), 0L), totals.get(index));
        }
    }

    @SuppressWarnings("null")
    @Test
    void returns30ZeroTotalsWhenThereAreNoRequests() {
        ApiRequestStatisticsService service = new ApiRequestStatisticsService(
                mock(ApiRequestStatisticRepository.class), mock(ApiRequestCounter.class));

        List<ApiRequestDailyTotal> totals = service.dailyTotals(List.of());

        assertEquals(30, totals.size());
        assertEquals(0L, totals.stream().mapToLong(ApiRequestDailyTotal::requestCount).sum());
    }
}