/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


/**
 * Quick and dirty hack for Date formatting
 */
public class DateFormatter {


    private static final DateTimeFormatter mmmFormatter = DateTimeFormatter.ofPattern("MMM.dd", Locale.GERMAN);
    private static final DateTimeFormatter ddFormatter = DateTimeFormatter.ofPattern("dd", Locale.GERMAN);

    private static final DateTimeFormatter dateDEFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.GERMAN);
    private static final DateTimeFormatter dateDEReverseFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMAN);
    private static final DateTimeFormatter dateTimeDEFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss", Locale.GERMAN);


    /**
     * @param date
     * @return Date String with German Format yyyy.MM.dd for sorting in Tables
     */
    public static String dateDE(LocalDate date) {
        if (date == null) return "";
        return date.format(dateDEFormatter);
    }


    /**
     * @param dateTime
     * @return Date and Time String with German Format yyyy.MM.dd HH:mm:ss
     */
    static String dateTimeDE(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(dateTimeDEFormatter);

    }


    /**
     * @param date
     * @return Date String with Format MM.dd for sorting in Tables
     */
    public static String dateMMMDD(LocalDate date) {
        if (date == null) return "";
        return date.format(mmmFormatter).substring(0, 3) + " " + date.format(ddFormatter);
    }


    /**
     * @param date
     * @return Date String with German Format, not for sorting but for display in text fields
     */
    public static String dateReverseDE(LocalDate date) {
        if (date == null) return "";
        return date.format(dateDEReverseFormatter);
    }


    /**
     * @param date
     * @return Date String with ISO Format yyyy-MM-dd for use with date type in html form
     */
    public static String formatISO(LocalDate date) {
        if (date == null) return "";
        return date.toString();
    }


}

