package eu.nabahilfe.webapp.timecheques;

import eu.nabahilfe.webapp.accountings.Accountable;
import eu.nabahilfe.webapp.accountings.AccountingEntry;
import eu.nabahilfe.webapp.members.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


/**
 * Zeitscheck - wird gekauft, zuerst angelegt und dann später verbucht vom Kassier
 */
@Entity
@Table(name = "time_cheques")
public class TimeCheque extends Accountable {
    @Column(nullable = false)
    private Integer hours;    // Anzahl der Stunden, üblicherweise 5 (Beitritt zum Verein) oder 10

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id")
    private Member assignedTo;    // wem werden die Stunden gutgeschrieben

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "accounting_entry_id")
    private AccountingEntry accountingEntry;    // referenz auf die Buchung, erst wenn verbucht ist


    // -------------------------------------------------
    // generate setter/getter methodes with Eclipse here
    // -------------------------------------------------

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Member getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Member assignedTo) {
        this.assignedTo = assignedTo;
    }

    public AccountingEntry getAccountingEntry() {
        return accountingEntry;
    }

    public void setAccountingEntry(AccountingEntry accountingEntry) {
        this.accountingEntry = accountingEntry;
    }


    // -----------------------------------------------
    // Don't forget to generate toString() for logging
    // -----------------------------------------------

    @Override
    public String toString() {
        return "TimeCheque [hours=" + hours + ", assignedTo=" + assignedTo + ", accountingEntry=" + accountingEntry
                + ", getAmount()=" + getAmount() + ", getOrderDate()=" + getOrderDate() + ", getCreatedAt()="
                + getCreatedAt() + ", getCreatedBy()=" + getCreatedBy() + ", getUpdatedAt()=" + getUpdatedAt()
                + ", getUpdatedBy()=" + getUpdatedBy() + "]";
    }



    // -------------------------------
    // add your business methodes here
    // -------------------------------


}
