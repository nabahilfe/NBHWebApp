package eu.nabahilfe.webapp.accountings;

import java.math.BigDecimal;
import java.time.LocalDate;

import eu.nabahilfe.webapp.members.Member;

public class AccountableRowSelectionForm implements Accountable {

    private String accountableClassName;    	// MemberFee, TimeCheque, Transaction, ...
    private Long accountableId;				// ID der konkreten Accountable Entity, also der accountableClass

    private Long accountableMemberId;    	// Nur bei Zeitscheck-Kauf oder Mitgliedsgebühr relevant, sonst null

    private String transactionType;    		// INCOME oder EXPENSE - muss aus Enum TransactionType kommen
    private String transactionISODate;
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

    public String getTransactionISODate() {
        return transactionISODate;
    }

    public void setTransactionDate(String transactionISODate) {
        this.transactionISODate = transactionISODate;
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
                + ", transactionISODate=" + transactionISODate + ", transactionAmount=" + transactionAmount + "]";
    }

}
