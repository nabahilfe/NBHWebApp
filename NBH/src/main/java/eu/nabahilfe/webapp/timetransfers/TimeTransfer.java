package eu.nabahilfe.webapp.timetransfers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.org.Offer;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

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
import jakarta.validation.constraints.Size;


/**
 * Zeitgutschrift von Mitglied A an Mitglied B für erbrachte Leistung.
 */
@Entity
@Table(name = "TIME_TRANSFERS")
public class TimeTransfer  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate dateOfService;    // Wann wurde die Leistung erbracht

    @Column(nullable = false)
    private Integer hours;    // Wie viele Stunden, mögliche Werte z.B. 1 .. 5

    @Size(max = 250)
    private String note;    // Anmerkung zur erbrachten Leistung

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id")
    private Offer offer;    // Art der erbrachten Leistung

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id")
    private Member fromMember;    // Leistungserbnringer

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id")
    private Member toMember;    // Leistungsempfänger

    // Creation timestamp, value is set by Postgres (see Table definition)
    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // FIXME in Generator: "@Column(nullable = false)"
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



    // --------------------------------------------------
    // generate setter/getter methodes with Eclipse here
    // --------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateOfService() {
        return dateOfService;
    }

    public void setDateOfService(LocalDate dateOfService) {
        this.dateOfService = dateOfService;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Member getFromMember() {
        return fromMember;
    }

    public void setFromMember(Member fromMember) {
        this.fromMember = fromMember;
    }

    public Member getToMember() {
        return toMember;
    }

    public void setToMember(Member toMember) {
        this.toMember = toMember;
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



    // --------------------------------
    // add your business methodes here
    // --------------------------------



    // ------------------------------------------------------------------------
    // generate equals/hasCode methodes with Eclipse here - use ONLY id field!
    // ------------------------------------------------------------------------

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
        TimeTransfer other = (TimeTransfer) obj;
        return Objects.equals(id, other.id);
    }


    // toString() for debugging

    @Override
    public String toString() {
        return "TimeTransfer [id=" + id + ", dateOfService=" + dateOfService + ", hours=" + hours + ", note=" + note
                + ", offer=" + offer + ", fromMember=" + fromMember + ", toMember=" + toMember + ", createdAt="
                + createdAt + ", createdBy=" + createdBy + ", updatedAt=" + updatedAt + ", updatedBy=" + updatedBy
                + ", version=" + version + "]";
    }





}
