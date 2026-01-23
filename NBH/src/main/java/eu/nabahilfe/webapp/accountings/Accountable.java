package eu.nabahilfe.webapp.accountings;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Interface für alles was verbucht wird (Zeitscheck kauf, Mitgliedschaft...)
 */
public interface Accountable {

    public abstract String 		getAccountableClassName();    	// MemberFee, TimeCheque, Transaction, ...
    public abstract Long 		getAccountableId();    			// ID der konkreten Accountable Entity, also der accountableClass

    public abstract Long 		getAccountableMemberId();    	// Nur bei Zeitscheck-Kauf oder Mitgliedsgebühr relevant, sonst null

    public abstract String 		getTransactionType();    		// INCOME oder EXPENSE - muss aus Enum TransactionType kommen
    public abstract String 		getTransactionISODate();    	// Date as ISO String
    public abstract BigDecimal 	getTransactionAmount();
}