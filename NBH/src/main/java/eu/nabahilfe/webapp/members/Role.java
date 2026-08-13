/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.members;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * Rollen im Verein. Über die Rollen werden auch die Berechtigungen vergeben.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ROLES")
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean isExecutiveMember;  // VEREINSROLLE: Vorstand - kann Mitglieder verwalten, Kassaführung einsehen und Content bearbeiten.

    @Column(nullable = false)
    private Boolean isAdmin;    		// ZUSATZ-ROLLE: Hat alle Rechte - es gibt immer einen sysadmin Account

    @Column(nullable = false)
    private Boolean isTreasurer;    	// VEREINSROLLE: Kassier - dokumentiert alle bezahlten Buchungen

    @Column(nullable = false)
    private Boolean isSecretary;    	// VEREINSROLLE: Schriftführer - kann Text-Content verwalten, z.B. News, Veranstaltungen, ...

    @Column(nullable = false)
    private Boolean isAuditor;    		// VEREINSROLLE: Rechnungsprüfer, muss unabhängig vom Vorstand sein, darf also kein Board Meber sein oder sonstige Rollen haben

    @Column(nullable = false)
    private Boolean isTimeKeeper;    	// ZUSATZ-ROLLE: Kann Zeit-Schescks vergeben / verkaufe und Zeiteschecks verbuchen - zusatzbereschtigung zb. auch für Vorstand

    @Column(nullable = false)
    private Boolean isMiscellaneous;    // SPEZIAL_ROLLE: Sonstiges, z.B. Ehrenmitglied

    @Size(max = 80)
    @NotEmpty
    @NotBlank(message = "Rollen-Name darf nicht leer sein!")
    private String roleName;    // Mitglied, Vorstand, stv. Vorstand, Kassier, stv. Kassier, Rechnungsprüfer, Schriftführer, ....

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


    // -------------------------------------------------
    // generate setter/getter methodes with Eclipse here
    // -------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsAdmin() {
        return isAdmin == null ? false : isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsExecutiveMember() {
        return isExecutiveMember;
    }

    public void setIsExecutiveMember(Boolean isExecutiveMember) {
        this.isExecutiveMember = isExecutiveMember;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName.trim();
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getIsAuditor() {
        return isAuditor;
    }

    public void setIsAuditor(Boolean isAuditor) {
        this.isAuditor = isAuditor;
    }

    public Boolean getIsTimeKeeper() {
        return isTimeKeeper;
    }

    public void setIsTimeKeeper(Boolean isTimeKeeper) {
        this.isTimeKeeper = isTimeKeeper;
    }

    public Boolean getIsMiscellaneous() {
        return isMiscellaneous == null ? false : isMiscellaneous;
    }

    public void setIsMiscellaneous(Boolean isMiscellaneous) {
        this.isMiscellaneous = isMiscellaneous;
    }

    public Boolean getIsTreasurer() {
        return isTreasurer == null ? false : isTreasurer;
    }

    public void setIsTreasurer(Boolean isTreasurer) {
        this.isTreasurer = isTreasurer;
    }

    public Boolean getIsSecretary() {
        return isSecretary == null ? false : isSecretary;
    }

    public void setIsSecretary(Boolean isSecretary) {
        this.isSecretary = isSecretary;
    }

    public boolean isSystemAdminiRole() {
        return NbhConst.ADMIN_ROLE_NAME.equalsIgnoreCase(roleName);
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
        Role other = (Role) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Role [id=" + id + ", isAdmin=" + isAdmin + ", isExecutiveMember=" + isExecutiveMember + ", isTreasurer="
                + isTreasurer + ", isSecretary=" + isSecretary + ", isAuditor=" + isAuditor + ", isTimeKeeper="
                + isTimeKeeper + ", isMiscellaneous=" + isMiscellaneous + ", roleName=" + roleName + ", version="
                + version + "]";
    }



    // -------------------------------
    // add your business methodes here
    // -------------------------------

    public Role() {
        this.isAdmin = false;
        this.isExecutiveMember = false;
        this.isAuditor = false;
        this.isTimeKeeper = false;
        this.isMiscellaneous = false;
        this.isTreasurer = false;
        this.isSecretary = false;
    }

    // for spring security

    public Set<String> getAuthorities() {

        Set<String> auths = new HashSet<>();

        auths.add("ROLE_USER");   					// Alle Rollen haben die Rolle USER

        if (Boolean.TRUE.equals(isAdmin)) {
            auths.add("ROLE_ADMIN");
        }

        if (Boolean.TRUE.equals(isExecutiveMember)) {	// Vorstand (Obmann, Obfrau und Stellvertreter) - kann Mitglieder verwalten, Kassaführung einsehen und Content bearbeiten.
            auths.add("ROLE_EXECUTIVE_MEMBER");
        }

        if (Boolean.TRUE.equals(isTreasurer)) {		// Kassier - kann Geld-Buchungen verwalten
            auths.add("ROLE_TREASURER");
        }

        if (Boolean.TRUE.equals(isSecretary)) {		// Schriftführer
            auths.add("ROLE_SECRETARY");
        }

        if (Boolean.TRUE.equals(isAuditor)) {		// Rechnungsprüfer, muss unabhängig vom Vorstand sein, darf also kein Board Meber sein oder sonstige rollen haben
            auths.add("ROLE_AUDITOR");
        }

        if (Boolean.TRUE.equals(isTimeKeeper)) {	// Kann Zeitschecks vergeben / verkaufen und Zeitschecks verbuchen
            auths.add("ROLE_TIME_KEEPER");
        }

        return auths;
    }

}