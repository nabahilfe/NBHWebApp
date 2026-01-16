package eu.nabahilfe.webapp.accountings;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Interface für alles was verbucht wird (Zeitscheck kauf, Mitgliedschaft...)
 */
public interface Accountable {
    public abstract String getAccountableClass();
    public abstract Long getAccountableId();
    public abstract String getTransactionType();
    public abstract LocalDate getTransactionDate();
    public abstract BigDecimal getTransactionAmount();
}
