package eu.nabahilfe.webapp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


/**
 * Quick and dirty hack for Date formatting
 */
public class DateFormatter {

    /**
     * @param date
     * @return Date String with German Format yyyy.MM.dd for sorting in Tables
     */
    public static String dateDE(LocalDate date) {
        if (date == null) return "";
        return date.format(
            DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.GERMAN)
        );
    }


    /**
     * @param date
     * @return Date String with German Format, not for sorting but for display in text fields
     */
    public static String dateReverseDE(LocalDate date) {
        if (date == null) return "";
        return date.format(
            DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMAN)
        );
    }


    /**
     * @param date
     * @return Date String with ISO Format yyyy-MM-dd for use with date type in html form
     */
    public static String formatISO(LocalDate date) {
        if (date == null) return "";
        return date.toString();
    }

    /**
     * @param dateTime
     * @return DateTime String with ISO Format yyyy-MM-dd HH:mm:ss
     */
    public static String dateTimeDE(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }


}

