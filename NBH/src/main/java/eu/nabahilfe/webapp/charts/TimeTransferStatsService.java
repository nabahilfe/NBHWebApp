/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.charts;

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
}
