package eu.nabahilfe.webapp.system;

import java.time.LocalDate;

public record ApiRequestStatisticKey(
        LocalDate date,
        String httpMethod,
        String urlPattern,
        int statusCode) {
}