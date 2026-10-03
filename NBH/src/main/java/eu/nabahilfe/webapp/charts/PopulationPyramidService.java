/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.charts;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import eu.nabahilfe.webapp.members.Salutation;

@Service
public class PopulationPyramidService {

    private static final int DECADES = 9;

    private final PopulationPyramidRepository populationPyramidRepository;

    public PopulationPyramidService(PopulationPyramidRepository populationPyramidRepository) {
        this.populationPyramidRepository = populationPyramidRepository;
    }

    /** Member counts by salutation and decade, index 0 = 80+, index 8 = 0-9. */
    public List<AgeDecadeData> ageDistribution() {
        LocalDate today = LocalDate.now();
        int[] maleCounts = new int[DECADES];
        int[] femaleCounts = new int[DECADES];

        for (MemberBirthdateSalutation member : populationPyramidRepository.findActiveMemberBirthdates()) {
            int age = Period.between(member.birthdate(), today).getYears();
            if (age < 0) {
                continue;
            }
            int index = DECADES - 1 - Math.min(age / 10, DECADES - 1);
            if (Salutation.Herr.name().equals(member.salutation())) {
                maleCounts[index]++;
            } else if (Salutation.Frau.name().equals(member.salutation())) {
                femaleCounts[index]++;
            }
        }

        List<AgeDecadeData> result = new ArrayList<>(DECADES);
        for (int i = 0; i < DECADES; i++) {
            result.add(new AgeDecadeData(maleCounts[i], femaleCounts[i]));
        }
        return List.copyOf(result);
    }
}
