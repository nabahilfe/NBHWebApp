package eu.nabahilfe.webapp.members;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


/**
 * Die Mitglieder des Vereins. Eine Mitgliedsnummer muss bei Neuanlage automatisch vergeben werden.
 */
@Entity
@Table(name = "MEMBERS")
public class Member  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer memberNmbr;    // Member ID muss automatisch erzeugt werden, Startwert 1000

    @Size(max = 20)
    private String salutation;    // Aus Enum Salutation

    @Size(max = 20)
    private String title;    // Titel, Freitext

    @Size(max = 80)
    private String institution;    // Institution die das Mitglied vertritt

    @Size(max = 80)
    @NotEmpty
    private String firstName;

    @Size(max = 80)
    @NotEmpty
    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate birthdate;

    @Size(max = 80)
    @Email
    private String email;    // muss immer in lower-case gespeichert werden!

    @Size(max = 250)
    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate joiningDate;    // Eintrittsdatum in den Verein

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate resignationDate;    // Austrittsdatum aus dem Verein

    @Size(max = 80)
    private String street;    // Adressdaten des Mitglieds

    @Size(max = 20)
    private String number;

    @Size(max = 10)
    private String zip;

    @Size(max = 80)
    private String city;

    @Column(nullable = false)
    private Boolean directDebitAuthorization;    // Wenn Einziehungsauftrag vorhanden kann Mitglied sebständig Zeitschecks bestellen

    private Integer accumulatedHours;    // Gut-Stunden - kommt aus Gutschrift bei Eintritt, Stundenkauf, Stundenerwerb durch Hilfestellung, ...

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "role_id")
    private Role role;    // Nur befüllt wenn zusätlich Rolle zum normalen Mitglied

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





    // --------------------------------------------------
    // generate setter/getter methodes with Eclipse here
    // --------------------------------------------------

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Integer getMemberNmbr() {
        return memberNmbr;
    }


    public void setMemberNmbr(Integer memberNmbr) {
        this.memberNmbr = memberNmbr;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public LocalDate getBirthdate() {
        return birthdate;
    }


    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public LocalDate getJoiningDate() {
        return joiningDate;
    }


    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }


    public LocalDate getResignationDate() {
        return resignationDate;
    }


    public void setResignationDate(LocalDate resignationDate) {
        this.resignationDate = resignationDate;
    }


    public String getStreet() {
        return street;
    }


    public void setStreet(String street) {
        this.street = street;
    }


    public String getNumber() {
        return number;
    }


    public void setNumber(String number) {
        this.number = number;
    }


    public String getZip() {
        return zip;
    }


    public void setZip(String zip) {
        this.zip = zip;
    }


    public String getCity() {
        return city;
    }


    public void setCity(String city) {
        this.city = city;
    }


    public Integer getAccumulatedHours() {
        return accumulatedHours;
    }


    public void setAccumulatedHours(Integer accumulatedHours) {
        this.accumulatedHours = accumulatedHours;
    }


    public Role getRole() {
        return role;
    }


    public void setRole(Role role) {
        this.role = role;
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


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getSalutation() {
        return salutation;
    }


    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getInstitution() {
        return institution;
    }


    public void setInstitution(String institution) {
        this.institution = institution;
    }


    public Boolean getDirectDebitAuthorization() {
        return directDebitAuthorization;
    }


    public void setDirectDebitAuthorization(Boolean directDebitAuthorization) {
        this.directDebitAuthorization = directDebitAuthorization;
    }


    // --------------------------------
    // add your business methodes here
    // --------------------------------



    public String getName() {
        return lastName + " " + firstName;
    }


    public String getAddress() {
        return street + " " + number + ", " + zip + " " + city;
    }


    public String getNameAndAddress() {
        return getName() + " - " + getAddress();
    }


    public String getEmailSalutation() {
        String name = "";
        if (salutation.equals(Salutation.Herr.name())) name += "Lieber ";
        else if (salutation.equals(Salutation.Frau.name())) name += "Liebe ";
        else name += "Hallo ";
        name += firstName + "!";
        return name;
    }


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
        Member other = (Member) obj;
        return Objects.equals(id, other.id);
    }


    @Override
    public String toString() {
        return "Member [id=" + id + ", memberNmbr=" + memberNmbr + ", salutation=" + salutation + ", title=" + title
                + ", institution=" + institution + ", firstName=" + firstName + ", lastName=" + lastName
                + ", birthdate=" + birthdate + ", email=" + email + ", password=" + password + ", joiningDate="
                + joiningDate + ", resignationDate=" + resignationDate + ", street=" + street + ", number=" + number
                + ", zip=" + zip + ", city=" + city + ", directDebitAuthorization=" + directDebitAuthorization
                + ", accumulatedHours=" + accumulatedHours + ", role=" + role + ", createdAt=" + createdAt
                + ", createdBy=" + createdBy + ", updatedAt=" + updatedAt + ", updatedBy=" + updatedBy + ", version="
                + version + "]";
    }

}
