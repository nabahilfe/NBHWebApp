package eu.nabahilfe.webapp.timecheques;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import eu.nabahilfe.webapp.accountings.Accountable;
import eu.nabahilfe.webapp.accountings.AccountingEntry;
import eu.nabahilfe.webapp.accountings.TransactionType;
import eu.nabahilfe.webapp.members.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;


/**
 * Zeitscheck - zuerst angelegt und dann später verbucht vom Kassier. TransactionType ist immer INCOME
 */
@Entity
@Table(name = "TIME_CHEQUES")
public class TimeCheque implements Accountable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer hours;    // Anzahl der Stunden, üblicherweise 5 (Beitritt zum Verein) oder 10

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate transactionDate;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id")
    private Member assignedTo;    // wem werden die Stunden gutgeschrieben

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "accounted_by_id")
    private AccountingEntry accountedBy;    // Abrechnung dokumentiert mit AccountingEntry

    // Creation timestamp, value is set by Postgres (see Table definition)
    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // FIXME: im Generator: "@Column(nullable = false)"
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "created_by_id")
    private Member createdBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "updated_by_id")
    private Member updatedBy;

    @Version
    @Column(nullable = false)
    private Integer version;

    @Override
    public String getAccountableClassName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Long getAccountableId() {
        return id;
    }


    @Override
    public String getTransactionType() {
        return TransactionType.INCOME.name();
    }

    @Override
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    @Override
    public BigDecimal getTransactionAmount() {
        return amount;
    }

    @Override
    public Long getAccountableMemberId() {
        return assignedTo.getId();
    }

    // --------------------------------
    // Getter and Setter
    // --------------------------------


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Member getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Member assignedTo) {
        this.assignedTo = assignedTo;
    }

    public AccountingEntry getAccountedBy() {
        return accountedBy;
    }

    public void setAccountedBy(AccountingEntry accountedBy) {
        this.accountedBy = accountedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Member getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Member createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Member getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Member updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }


    @Override
    public String toString() {
        return "TimeCheque [getAccountableClass()=" + getAccountableClassName() + ", getAccountableId()="
                + getAccountableId() + ", getTransactionType()=" + getTransactionType() + ", getTransactionDate()="
                + getTransactionDate() + ", getTransactionAmount()=" + getTransactionAmount() + ", getId()=" + getId()
                + ", getHours()=" + getHours() + ", getAmount()=" + getAmount() + ", getAssignedTo()=" + getAssignedTo()
                + ", getAccountedBy()=" + getAccountedBy() + ", getCreatedAt()=" + getCreatedAt() + ", getCreatedBy()="
                + getCreatedBy() + ", getUpdatedAt()=" + getUpdatedAt() + ", getUpdatedBy()=" + getUpdatedBy()
                + ", getVersion()=" + getVersion() + "]";
    }





    // -----------------------------------------------
    // Don't forget to generate toString() for logging
    // -----------------------------------------------






    // -------------------------------
    // add your business methodes here
    // -------------------------------


}
