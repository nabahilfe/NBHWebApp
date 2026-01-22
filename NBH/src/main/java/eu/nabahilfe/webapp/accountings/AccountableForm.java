package eu.nabahilfe.webapp.accountings;

import java.math.BigDecimal;
import java.time.LocalDate;

import eu.nabahilfe.webapp.members.Member;

public class AccountableForm implements Accountable {

    private String accountableClass;    	// MemberFee, TimeCheque, Transaction, ...
    private Long accountableId;				// ID der konkreten Accountable Entity, also der accountableClass

    private Member accountableMember;    	// Nur bei Zeitscheck-Kauf oder Mitgliedsgebühr relevant, sonst null

    private String transactionType;    		// INCOME oder EXPENSE - muss aus Enum TransactionType kommen
    private LocalDate transactionDate;
    private BigDecimal transactionAmount;



    public String getAccountableClass() {
        return accountableClass;
    }

    public void setAccountableClass(String accountableClass) {
        this.accountableClass = accountableClass;
    }

    public Long getAccountableId() {
        return accountableId;
    }

    public void setAccountableId(Long accountableId) {
        this.accountableId = accountableId;
    }

    public Member getAccountableMember() {
        return accountableMember;
    }

    public void setAccountableMember(Member accountableMember) {
        this.accountableMember = accountableMember;
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
        return "AccountableForm [accountableClass=" + accountableClass + ", accountableId=" + accountableId
                + ", accountableMember=" + accountableMember.getName() + ", transactionType=" + transactionType
                + ", transactionDate=" + transactionDate + ", transactionAmount=" + transactionAmount + "]";
    }

}
