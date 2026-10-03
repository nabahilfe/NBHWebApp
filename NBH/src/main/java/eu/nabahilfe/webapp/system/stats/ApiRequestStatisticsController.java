// src/main/java/eu/nabahilfe/webapp/system/stats/ApiRequestStatisticsController.java
package eu.nabahilfe.webapp.system.stats;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/system/stats")
public class ApiRequestStatisticsController {

    private final ApiRequestStatisticsService statisticsService;
    private final ObjectMapper objectMapper;

    public ApiRequestStatisticsController(ApiRequestStatisticsService statisticsService, ObjectMapper objectMapper) {
        this.statisticsService = statisticsService;
        this.objectMapper = objectMapper;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EXECUTIVE_MEMBER', 'BOARD_MEMBER')")
    @GetMapping("/api-requests")
    public String showApiRequests(Model model) {
        List<ApiRequestStatistic> statistics = statisticsService.findLast30Days();
        model.addAttribute("statistics", statistics);
        try {
            model.addAttribute("dailyTotalsJson", objectMapper.writeValueAsString(statisticsService.dailyTotals(statistics)));
        } catch (JacksonException exception) {
            throw new IllegalStateException("Could not serialize daily API request statistics", exception);
        }
        return "system/stats/api-requests";
    }
}