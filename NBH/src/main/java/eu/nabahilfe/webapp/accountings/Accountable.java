package eu.nabahilfe.webapp.accountings;

import java.math.BigDecimal;
import java.time.LocalDate;

import eu.nabahilfe.webapp.members.Member;

/**
 * Interface für alles was verbucht wird (Zeitscheck kauf, Mitgliedschaft...)
 */
public interface Accountable {
    public abstract String getAccountableClass();       // MemberFee, TimeCheque, Transaction, ...
    public abstract Long getAccountableId();
    public abstract Member getAccountableMember();      // Nur bei Zeitscheck-Kauf oder Mitgliedsgebühr relevant, sonst null
    public abstract String getTransactionType();        // INCOME oder EXPENSE - muss aus Enum TransactionType kommen
    public abstract LocalDate getTransactionDate();
    public abstract BigDecimal getTransactionAmount();
}
