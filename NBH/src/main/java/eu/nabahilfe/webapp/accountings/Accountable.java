/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.accountings;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Interface für alles was verbucht wird (Zeitscheck kauf, Mitgliedschaft...)
 */
public interface Accountable {

    public abstract String getAccountableName();   		// Mitgliedsbeitrag, Zeitscheck, Sonstiges, ...
    public abstract Long getAccountableId();    		// ID der konkreten Accountable Entity, also der accountableName

    public abstract Long getAccountableMemberId();    	// Nur bei Zeitscheck-Kauf oder Mitgliedsgebühr relevant, sonst null

    public abstract String getTransactionType();    	// INCOME oder EXPENSE - muss aus Enum TransactionType kommen
    public abstract LocalDate getTransactionDate();
    public abstract BigDecimal getTransactionAmount();
    public abstract String getLiableMemberName();    	// Wer hat das veranlasst oder angeordnet -> Name von cretaedBy Member
    public abstract void setLiableMemberName(String name);

    public abstract String getTransactionISODate();    	// Date of transaction as ISO Date String
}