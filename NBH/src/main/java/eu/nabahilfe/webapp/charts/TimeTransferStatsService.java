/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.charts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import eu.nabahilfe.webapp.timetransfers.TimeTransferRepository;

@Service
public class TimeTransferStatsService {

    private final TimeTransferRepository timeTransferRepository;

    public TimeTransferStatsService(TimeTransferRepository timeTransferRepository) {
        this.timeTransferRepository = timeTransferRepository;
    }

    public List<TimeTransferOfferStats> offerDistribution(int year) {
        return timeTransferRepository.findStatsByOfferAndYear(year).stream()
                .map(row -> new TimeTransferOfferStats(
                        row[0] + " - " + row[1],
                        row[2] == null ? 0L : ((Number) row[2]).longValue()))
                .toList();
    }

    public List<Long> monthlyHours(int year) {
        List<Long> totals = new ArrayList<>(Collections.nCopies(12, 0L));
        for (Object[] row : timeTransferRepository.findMonthlyHoursByYear(year)) {
            int monthIndex = ((Number) row[0]).intValue() - 1;
            totals.set(monthIndex, row[1] == null ? 0L : ((Number) row[1]).longValue());
        }
        return List.copyOf(totals);
    }
}
