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
        when(service.findLast30Days()).thenReturn(statistics);
        ExtendedModelMap model = new ExtendedModelMap();

        assertEquals("system/stats/api-requests", new ApiRequestStatisticsController(service).showApiRequests(model));
        assertSame(statistics, model.getAttribute("statistics"));
        verify(service).findLast30Days();
    }
}