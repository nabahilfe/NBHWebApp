/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.charts;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/charts")
public class ChartController {

    private final PopulationPyramidService populationPyramidService;
    private final TimeTransferStatsService timeTransferStatsService;
    private final ObjectMapper objectMapper;

    public ChartController(PopulationPyramidService populationPyramidService,
            TimeTransferStatsService timeTransferStatsService, ObjectMapper objectMapper) {
        this.populationPyramidService = populationPyramidService;
        this.timeTransferStatsService = timeTransferStatsService;
        this.objectMapper = objectMapper;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EXECUTIVE_MEMBER', 'BOARD_MEMBER')")
    @GetMapping("/population-piramid/age-distribution")
    public String showAgeDistribution(final Model model) {
        model.addAttribute("chartData", populationPyramidService.ageDistribution());
        return "charts/population-piramid";
    }

    @GetMapping("/timecheques/distribution")
    public String showTimeTransferDistribution(@RequestParam(required = false) Integer year, final Model model) {
        int currentYear = LocalDate.now().getYear();
        int selectedYear = year != null && year >= currentYear - 7 && year <= currentYear ? year : currentYear;
        List<Integer> years = new ArrayList<>(8);
        for (int optionYear = currentYear; optionYear >= currentYear - 7; optionYear--) {
            years.add(optionYear);
        }

        List<TimeTransferOfferStats> chartData = timeTransferStatsService.offerDistribution(selectedYear);
        try {
            model.addAttribute("chartDataJson", objectMapper.writeValueAsString(chartData));
        } catch (JacksonException e) {
            throw new IllegalStateException("Could not serialize time transfer chart data", e);
        }
        model.addAttribute("hasData", !chartData.isEmpty());
        model.addAttribute("years", years);
        model.addAttribute("selectedYear", selectedYear);
        return "charts/timetransfer-stats";
    }
}
