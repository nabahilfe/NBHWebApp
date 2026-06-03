package eu.nabahilfe.webapp.members;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Objects;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import eu.nabahilfe.webapp.accountings.Accountable;
import eu.nabahilfe.webapp.accountings.AccountingEntry;
import eu.nabahilfe.webapp.accountings.TransactionType;
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
 * Dokumentation des jährlichen Mitgliedsbeitrag. TransactionType ist immer INCOME
 */
@Entity
@Table(name = "MEMBERSHIP_FEES")
public class MembershipFee implements Accountable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Year forYear;

    @Column(nullable = false)
    private Boolean doNotCharge;    // z.B. für Ehrenmitglieder

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate transactionDate;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "accounted_by_id")
    private AccountingEntry accountedBy;    // Abrechnung dokumentiert mit AccountingEntry

    // Creation timestamp, value is set by Postgres (see Table definition)
    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // FIXME: im Generator: "@Column(nullable = false)"
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
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
    public Long getAccountableMemberId() {
        return member != null ? member.getId() : null;
    }

    @Override
    public String getTransactionType() {
        return TransactionType.INCOME.name();
    }


    @Override
    public BigDecimal getTransactionAmount() {
        return amount;
    }

    @Override
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    @Override
    public String getTransactionISODate() {
        return transactionDate != null ? transactionDate.toString() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Year getForYear() {
        return forYear;
    }

    public void setForYear(Year forYear) {
        this.forYear = forYear;
    }

    public Boolean getDoNotCharge() {
        return doNotCharge;
    }

    public void setDoNotCharge(Boolean doNotCharge) {
        this.doNotCharge = doNotCharge;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
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
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MembershipFee other = (MembershipFee) obj;
        return Objects.equals(id, other.id);
    }





    // -----------------------------------------------
    // Don't forget to generate toString() for logging
    // -----------------------------------------------



    // ------------------------------
    // add your business methods here
    // ------------------------------



}
