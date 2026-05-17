/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.domaintypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * Fachliche konfigurierbare Parameter für Kosten Mitgliedsbeitrag oder Zeitschecks. Der Code wird in einem ENUM definiert
 */
@Entity
@Table(name = "AMOUNT_DOMAIN_VALUES")
public class AmountDomainValue  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 20)
    @NotEmpty
    private String code;    // aus Enum, z.B TIMECHEQUE MEMBER_FEE, ...

    @Column(nullable = false)
    private BigDecimal amount;    // Kosten der jeweiligen Leistung im Gültigkeitszeitraum

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate validFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate validTo;    // letzter Eintrag hat immer 9999-12-31

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



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
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
        AmountDomainValue other = (AmountDomainValue) obj;
        return Objects.equals(id, other.id);
    }



    @Override
    public String toString() {
        return "AmountDomainValue [id=" + id + ", code=" + code + ", amount=" + amount + ", validFrom=" + validFrom
                + ", validTo=" + validTo + ", createdAt=" + createdAt + ", createdBy=" + createdBy + ", updatedAt="
                + updatedAt + ", updatedBy=" + updatedBy + ", version=" + version + "]";
    }


    // ------------------------------
    // add your business methods here
    // ------------------------------



}
