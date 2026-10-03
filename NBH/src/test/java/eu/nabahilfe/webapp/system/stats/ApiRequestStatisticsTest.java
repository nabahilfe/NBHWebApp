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
        List<ApiRequestDailyTotal> dailyTotals = List.of(new ApiRequestDailyTotal(LocalDate.now(), 0L));
        when(service.findLast30Days()).thenReturn(statistics);
        when(service.dailyTotals(statistics)).thenReturn(dailyTotals);
        ObjectMapper objectMapper = new ObjectMapper();
        ExtendedModelMap model = new ExtendedModelMap();

        assertEquals("system/stats/api-requests", new ApiRequestStatisticsController(service, objectMapper).showApiRequests(model));
        assertSame(statistics, model.getAttribute("statistics"));
        assertEquals(objectMapper.writeValueAsString(dailyTotals), model.getAttribute("dailyTotalsJson"));
        verify(service).findLast30Days();
        verify(service).dailyTotals(statistics);
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

    @Test
    void returns30ZeroTotalsWhenThereAreNoRequests() {
        ApiRequestStatisticsService service = new ApiRequestStatisticsService(
                mock(ApiRequestStatisticRepository.class), mock(ApiRequestCounter.class));

        List<ApiRequestDailyTotal> totals = service.dailyTotals(List.of());

        assertEquals(30, totals.size());
        assertEquals(0L, totals.stream().mapToLong(ApiRequestDailyTotal::requestCount).sum());
    }
}