package eu.nabahilfe.webapp.timecheques;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import eu.nabahilfe.webapp.accountings.Accountable;
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


@Entity
@Table(name = "time_cheques")
public class TimeCheque implements Accountable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Integer hours;

    BigDecimal amount;

    LocalDate orderDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assigned_to")
    Member assignedTo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by")
    Member createdBy;

    @Version
    @Column(nullable = false)
    Integer version;

    public TimeCheque() {
        super();
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

    public Member getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Member createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        TimeCheque other = (TimeCheque) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "TimeCheque [id=" + id + ", hours=" + hours + ", amount=" + amount + ", assignedTo=" + assignedTo
                + ", createdBy=" + createdBy + ", version=" + version + "]";
    }

    @Override
    public String getAccounableEntityName() {
        return getClass().getName();
    }


}
