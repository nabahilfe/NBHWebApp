/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.accountings;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountableRowSelectionForm implements Accountable {

    private String accountableClassName;    // MemberFee, TimeCheque, Transaction, ...
    private Long accountableId;				// ID der konkreten Accountable Entity, also der accountableClass

    private Long accountableMemberId;    	// Nur bei Zeitscheck-Kauf oder Mitgliedsgebühr relevant, sonst null

    private String transactionType;    		// INCOME oder EXPENSE - muss aus Enum TransactionType kommen
    private LocalDate transactionDate;
    private BigDecimal transactionAmount;



    public String getAccountableClassName() {
        return accountableClassName;
    }

    public void setAccountableClassName(String accountableClassName) {
        this.accountableClassName = accountableClassName;
    }

    public Long getAccountableId() {
        return accountableId;
    }

    public void setAccountableId(Long accountableId) {
        this.accountableId = accountableId;
    }

    public Long getAccountableMemberId() {
        return accountableMemberId;
    }

    public void setAccountableMemberId(Long accountableMemberId) {
        this.accountableMemberId = accountableMemberId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionISODate() {
        return transactionDate != null ? transactionDate.toString() : null;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }


    @Override
    public String toString() {
        return "AccountableForm [accountableClassName=" + accountableClassName + ", accountableId=" + accountableId
                + ", accountableMemberId=" + accountableMemberId + ", transactionType=" + transactionType
                + ", transactionDate=" + transactionDate + ", transactionAmount=" + transactionAmount + "]";
    }


}
