package eu.nabahilfe.webapp.timeexchange;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import eu.nabahilfe.webapp.NbhConst;
import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.org.Offer;
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


@Entity
@Table(name = "time_transfers")
public class TimeTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    LocalDate dateOfService;

    @Column(nullable = false)
    Integer hours;

    @Size(max = NbhConst.MAX_LEN_SHORT_STRING)
    String note;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "offer")
    Offer offer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_member")
    Member fromMember;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_member")
    Member toMember;

    @Version
    @Column(nullable = false)
    Integer version;

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

    public LocalDate getDateOfService() {
        return dateOfService;
    }

    public String getDateOfServiceString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return dateOfService.format(formatter);
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

    @Override
    public String toString() {
        return "TimeTransfer [id=" + id + ", dateOfService=" + dateOfService + ", hours=" + hours + ", note=" + note
                + ", offer=" + offer + ", fromMember=" + fromMember + ", toMember=" + toMember + ", version=" + version
                + "]";
    }

}
