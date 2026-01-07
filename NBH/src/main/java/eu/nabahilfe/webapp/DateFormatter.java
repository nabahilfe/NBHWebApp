package eu.nabahilfe.webapp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormatter {
    public static String formatDE(LocalDate date) {
        if (date == null) return "";
        return date.format(
            DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.GERMAN)
        );
    }
}

