/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.accountings;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import eu.nabahilfe.webapp.members.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


/**
 * Buchungsdatensatz zu Zeitscheck-Kauf, Mitgliedschaft, Weihnachtsessen, usw.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ACCOUNTING_ENTRIES")
public class AccountingEntry  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 80)
    private String accountableName;    // MemberFee, TimeCheque, Transaction, ...

    private Long accountableId;    // id zur Klasse bzw. Tabelle

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "accountable_member_id")
    private Member accountableMember;    // id zum Mitglied, optional, nur bei Zeitscheck-Kauf, Mitgliedsbeitrag

    @Size(max = 10)
    @NotEmpty
    private String transactionType;    // INCOME oder EXPENSE - muss aus Enum TransactionType kommen

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate transactionDate;    // Buchungsdatum

    @Column(nullable = false)
    private BigDecimal transactionAmount;    // Betrag

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate accountingDate;    // Buchungsdatum

    @Size(max = 250)
    private String description;    // Verpflichtend wenn kein fix definierter Name wie 'Zeitscheck', 'Mitgliedsgebühr' verwendet wird, sondern 'Sonstiges'

    // Creation timestamp, value is set by Postgres (see Table definition)
    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // FIXME: im Generator: "@Column(nullable = false)"
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "created_by_id")
    @CreatedBy
    private Member createdBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "updated_by_id")
    @LastModifiedBy
    private Member updatedBy;

    @Version
    @Column(nullable = false)
    private Integer version;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountableName() {
        return accountableName;
    }

    public void setAccountableName(String accountableName) {
        this.accountableName = accountableName;
    }

    public Long getAccountableId() {
        return accountableId;
    }

    public void setAccountableId(Long accountableId) {
        this.accountableId = accountableId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getTransactionDisplayType() {
        if (transactionType.contains(TransactionType.INCOME.name())) {
            return "Einnahme";
        } else if (transactionType.contains(TransactionType.EXPENSE.name())) {
            return "Ausgabe";
        } else {
            return transactionType;
        }
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

    public LocalDate getAccountingDate() {
        return accountingDate;
    }

    public void setAccountingDate(LocalDate accountingDate) {
        this.accountingDate = accountingDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Member getAccountableMember() {
        return accountableMember;
    }

    public void setAccountableMember(Member accountableMember) {
        this.accountableMember = accountableMember;
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
        AccountingEntry other = (AccountingEntry) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "AccountingEntry [id=" + id + ", accountableName=" + accountableName + ", accountableId="
                + accountableId + ", accountableMember=" + accountableMember + ", transactionType=" + transactionType
                + ", transactionDate=" + transactionDate + ", transactionAmount=" + transactionAmount
                + ", accountingDate=" + accountingDate + ", description=" + description + ", createdAt=" + createdAt
                + ", createdBy=" + createdBy + ", updatedAt=" + updatedAt + ", updatedBy=" + updatedBy + ", version="
                + version + "]";
    }


    // ------------------------------
    // add your business methods here
    // ------------------------------


}
