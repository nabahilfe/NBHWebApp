// src/main/java/eu/nabahilfe/webapp/system/stats/ApiRequestDailyTotal.java
package eu.nabahilfe.webapp.system.stats;

import java.time.LocalDate;

public record ApiRequestDailyTotal(LocalDate requestDate, long requestCount) {
}