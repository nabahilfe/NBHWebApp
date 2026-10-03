/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.charts;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/charts")
public class ChartController {

    private final PopulationPyramidService populationPyramidService;

    public ChartController(PopulationPyramidService populationPyramidService) {
        this.populationPyramidService = populationPyramidService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EXECUTIVE_MEMBER', 'BOARD_MEMBER')")
    @GetMapping("/population-piramid/age-distribution")
    public String showAgeDistribution(final Model model) {
        model.addAttribute("chartData", populationPyramidService.ageDistribution());
        return "charts/population-piramid";
    }
}
