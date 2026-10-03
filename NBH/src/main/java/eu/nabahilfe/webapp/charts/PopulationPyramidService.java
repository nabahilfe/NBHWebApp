/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.charts;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PopulationPyramidService {

    private static final int DECADES = 9;

    private final PopulationPyramidRepository populationPyramidRepository;

    public PopulationPyramidService(PopulationPyramidRepository populationPyramidRepository) {
        this.populationPyramidRepository = populationPyramidRepository;
    }

    /** Member counts per decade, index 0 = 80+, index 8 = 0-9 (order expected by the chart template). */
    public List<Integer> ageDistribution() {
        LocalDate today = LocalDate.now();
        int[] counts = new int[DECADES];

        for (LocalDate birthdate : populationPyramidRepository.findActiveMemberBirthdates()) {
            int age = Period.between(birthdate, today).getYears();
            if (age < 0) {
                continue;
            }
            int decade = Math.min(age / 10, DECADES - 1);
            counts[DECADES - 1 - decade]++;
        }

        return Arrays.stream(counts).boxed().toList();
    }
}
