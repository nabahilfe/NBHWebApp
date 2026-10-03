// src/main/java/eu/nabahilfe/webapp/system/stats/ApiRequestStatisticsController.java
package eu.nabahilfe.webapp.system.stats;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/system/stats")
public class ApiRequestStatisticsController {

    private final ApiRequestStatisticsService statisticsService;

    public ApiRequestStatisticsController(ApiRequestStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EXECUTIVE_MEMBER', 'BOARD_MEMBER')")
    @GetMapping("/api-requests")
    public String showApiRequests(Model model) {
        model.addAttribute("statistics", statisticsService.findLast30Days());
        return "system/stats/api-requests";
    }
}