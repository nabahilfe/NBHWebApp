/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.members;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import eu.nabahilfe.webapp.NbhConst;
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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


/**
 * Die Mitglieder des Vereins. Eine Mitgliedsnummer muss bei Neuanlage automatisch vergeben werden.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "MEMBERS")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Size(max = 30)
    private String phoneNumber;    // Telefonnummer des Mitglieds

    @Size(max = 250)
    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate joiningDate;    // Eintrittsdatum in den Verein

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate resignationDate;    // Austrittsdatum aus dem Verein

    @Size(max = 80)
    private String street;    	// Adressdaten des Mitglieds - Straße

    @Size(max = 20)
    private String number;    	// Hausunummer

    @Size(max = 20)
    private String stair;    	// Stiege

    @Size(max = 20)
    private String door;    	// Tür

    @Size(max = 10)
    private String zip;

    @Size(max = 80)
    private String city;

    private Double latitude;   	// Aus der Adressvalidierung

    private Double longitude;   // Aus der Adressvalidierung

    /** 1 if address is geo-validated (lat + lon both set), 0 otherwise. Used for sorting only. */
    @Formula("CASE WHEN latitude IS NOT NULL AND longitude IS NOT NULL THEN 1 ELSE 0 END")
    private Integer isGeoValidated;


    @Column(nullable = false)
    private Boolean directDebitAuthorization;    // Wenn Einziehungsauftrag vorhanden kann Mitglied sebständig Zeitschecks bestellen

    @Column(nullable = false)
    private Boolean isImportedMember;    // Für importierte, bestehende Mitglider muss das TRUE sein, damit ihnen kein Gratis-Zeitschecks zugeteilt werden kann und keine Mitgliedsbeeträge berechnet werden.

    @Column(nullable = false)
    private Boolean isSystemAccount;    // Für SystemAccounts wie SysAdmin und Sozialkonto muss TRUE verwendet werden

    private Integer accumulatedHours;    // Gut-Stunden - kommt aus Gutschrift bei Eintritt, Stundenkauf, Stundenerwerb durch Hilfestellung, ...

    @ManyToOne(fetch = FetchType.EAGER, optional = true)	// FetchType.EAGER damit Login funktioniert, da die Rolle für die Autorisierung benötigt wird.
    @JoinColumn(name = "role_id")
    private Role role;    // Nur befüllt wenn zusätlich Rolle zum normalen Mitglied

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


    // Email muss immer in lower-case gespeichert werden und darf nicht leer sein, sondern nur null
    public void setEmail(String email) {
        if (email != null) {
            email = email.trim().toLowerCase();
            if (email.length() == 0) email = null;
        }
        this.email = email;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getEinziehungsauftrgJaNein() {
        if (Boolean.TRUE.equals(directDebitAuthorization)) return "JA";
        return "NEIN";
    }


    public void setDirectDebitAuthorization(Boolean directDebitAuthorization) {
        this.directDebitAuthorization = directDebitAuthorization;
    }


    public boolean isImportedMember() {
        return isImportedMember != null && isImportedMember.booleanValue();
    }


    public void setIsImportedMember(Boolean isImportedMember) {
        this.isImportedMember = isImportedMember;
    }


    public Double getLatitude() {
        return latitude;
    }


    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }


    public Double getLongitude() {
        return longitude;
    }


    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    public boolean isSystemAccount() {
        return isSystemAccount != null && isSystemAccount.booleanValue();
    }


    public void setIsSystemAccount(Boolean isSystemAccount) {
        this.isSystemAccount = isSystemAccount;
    }


    public String getStair() {
        return stair;
    }


    public void setStair(String stair) {
        this.stair = stair;
    }


    public String getDoor() {
        return door;
    }


    public void setDoor(String door) {
        this.door = door;
    }



    // --------------------------------
    // add your business methodes here
    // --------------------------------



    public String getName() {
        return lastName + " " + firstName;
    }


    public String getAddress() {
        return street + " " + number + (stair != null && !stair.isEmpty() ? "/" + stair : "") + (door != null && !door.isEmpty() ? "/" + door : "") + ", " + zip + " " + city;
    }


    public String getNameAndAddress() {
        return getName() + " - " + getAddress();
    }


    // default system admin that may not be modified or deleted, is identified by firstName and lastName and role.isAdmin = true
    public boolean isSystemAdmin() {
        if (isSystemAccount() && NbhConst.ADMIN_ACCOUNT_FIRST_NAME.equalsIgnoreCase(firstName) && NbhConst.ADMIN_ACCOUNT_LAST_NAME.equalsIgnoreCase(lastName)) {
            return true;
        }
        return false;
    }

    public boolean isAdmin() {
        return role != null && role.getIsAdmin();
    }

    public boolean isSozialkonto() {
        if (isSystemAccount() && NbhConst.SOZIALKONTO_FIRST_NAME.equalsIgnoreCase(firstName) && NbhConst.SOZIALKONTO_LAST_NAME.equalsIgnoreCase(lastName)) {
            return true;
        }
        return false;
    }

    public boolean isSystemMember() {
        return isSystemAccount();
    }


    public String getEmailSalutation() {
        String name = "";
        if (Salutation.Herr.name().equals(salutation)) name += "Lieber ";
        else if (Salutation.Frau.name().equals(salutation)) name += "Liebe ";
        else name += "Hallo ";
        name += firstName + " " + lastName + "!";
        return name;
    }


    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return resignationDate == null || resignationDate.isAfter(today.minusDays(1));
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
                + ", isImportedMember=" + isImportedMember
                + ", accumulatedHours=" + accumulatedHours + ", role=" + role + ", createdAt=" + createdAt
                + ", createdById=" + (createdBy == null ? "" : createdBy.getId()) + ", updatedAt=" + updatedAt
                + ", updatedById=" + (updatedBy == null ? "" : updatedBy.getId()) + ", version="
                + version + "]";
    }


}