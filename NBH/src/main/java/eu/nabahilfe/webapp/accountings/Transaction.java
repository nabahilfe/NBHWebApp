package eu.nabahilfe.webapp.accountings;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import eu.nabahilfe.webapp.LiableMemberListener;
import eu.nabahilfe.webapp.NbhConst;
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
 * Allgemeine Einnahme oder Ausgabe
 */
@Entity
@EntityListeners({AuditingEntityListener.class, LiableMemberListener.class})
@Table(name = "TRANSACTIONS")
public class Transaction implements Accountable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 10)
    @NotEmpty
    private String transactionType;    // INCOME oder EXPENSE - muss aus Enum TransactionType kommen

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate transactionDate;

    @Column(nullable = false)
    private BigDecimal amount;

    @Size(max = 80)
    @NotEmpty
    private String liableMemberName;    // Wer hat das veranlasst oder angeordnet -> Name von cretaedBy Member

    @Size(max = 250)
    @NotEmpty
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "accounted_by_id")
    private AccountingEntry accountedBy;    // Abrechnung dokumentiert mit AccountingEntry

    // Creation timestamp, value is set by Postgres (see Table definition)
    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by_id", nullable = false)
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

    @Override
    public String getAccountableName() {
        return NbhConst.MISC_ACCOUNTING_NAME;
    }

    @Override
    public Long getAccountableId() {
        return id;
    }

    @Override
    public Long getAccountableMemberId() {
        return null;
    }

    @Override
    public String getTransactionType() {
        return transactionType;
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
    public String getTransactionISODate() {
        return DateTimeFormatter.ISO_DATE.format(transactionDate);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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


    @Override
    public String getLiableMemberName() {
        return liableMemberName;
    }

    public void setLiableMemberName(String name) {
        liableMemberName = name;
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

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
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
        Transaction other = (Transaction) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Transaction [id=" + id + ", transactionType=" + transactionType + ", transactionDate=" + transactionDate
                + ", amount=" + amount + ", description=" + description + ", accountedBy=" + accountedBy
                + ", createdAt=" + createdAt + ", createdBy=" + createdBy + ", updatedAt=" + updatedAt + ", updatedBy="
                + updatedBy + ", version=" + version + "]";
    }



    // ------------------------------
    // add your business methods here
    // ------------------------------



}
