/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.timetransfers;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Transfer object (DTO) for a single row of the "fremd-verbuchte Zeitschecks" booking report.
 * Populated directly by a JPQL constructor expression in {@link TimeTransferRepository}, so that
 * all null-handling / string concatenation happens in the query, not in the view layer.
 */
public class TimeTransferBookingReportRow {

    private final LocalDateTime createdAt;
    private final String createdByName;
    private final String fromMemberName;
    private final String toMemberName;
    private final LocalDate dateOfService;
    private final Integer hours;
    private final String category;
    private final String note;

    public TimeTransferBookingReportRow(LocalDateTime createdAt, String createdByName, String fromMemberName,
            String toMemberName, LocalDate dateOfService, Integer hours, String category, String note) {
        this.createdAt = createdAt;
        this.createdByName = createdByName;
        this.fromMemberName = fromMemberName;
        this.toMemberName = toMemberName;
        this.dateOfService = dateOfService;
        this.hours = hours;
        this.category = category;
        this.note = note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public String getFromMemberName() {
        return fromMemberName;
    }

    public String getToMemberName() {
        return toMemberName;
    }

    public LocalDate getDateOfService() {
        return dateOfService;
    }

    public Integer getHours() {
        return hours;
    }

    public String getCategory() {
        return category;
    }

    public String getNote() {
        return note;
    }

    public boolean hasNote() {
        return note != null && !note.isBlank();
    }

}
