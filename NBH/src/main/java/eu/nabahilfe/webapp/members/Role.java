package eu.nabahilfe.webapp.members;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.UpdateTimestamp;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * Rollen im Verein. Über die Rollen werden auch die Berechtigungen vergeben.
 */
@Entity
@Table(name = "ROLES")
public class Role  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean isBoardMember;    // Hat eine Funktion wie 'Vorstand', 'Kassier' usw. Muss bei der Rolle vergeben werden

    @Column(nullable = false)
    private Boolean isAdmin;    // Hat weitgehende Rechte, kann Mitglieder verwalten und Zeitschecks ausstellen

    @Column(nullable = false)
    private Boolean isTreasurer;    // Verwaltet das Geld, Kassier

    @Column(nullable = false)
    private Boolean isSecretary;    // Schriftführer

    @Column(nullable = false)
    private Boolean isAuditor;    // Rechnungsprüfer, muss unabhängig vom Vorstand sein, darf also kein Board Meber sein oder sonstige rollen haben

    @Column(nullable = false)
    private Boolean isTimeKeeper;    // Kann Zeit-Schescks vergeben / verkaufe und Zeiteschecks verbuchen

    @Column(nullable = false)
    private Boolean isMiscellaneous;    // Sonstiges, z.B. Ehrenmitglied

    @Size(max = 80)
    @NotEmpty
    @NotBlank(message = "Rollen-Name darf nicht leer sein!")
    private String roleName;    // Mitglied, Vorstand, stv. Vorstand, Kassier, stv. Kassier, Rechnungsprüfer, Schriftführer, ....

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
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsBoardMember() {
        return isBoardMember;
    }

    public void setIsBoardMember(Boolean isBoardMember) {
        this.isBoardMember = isBoardMember;
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

    public String adminSymbol() {
        if (isAdmin) return "✔️";
        return "";
    }

    public String boardMemberSymbol() {
        if (isBoardMember) return "✔️";
        return "";
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
        return "Role [id=" + id + ", isAdmin=" + isAdmin + ", isBoardMember=" + isBoardMember + ", isTreasurer="
                + isTreasurer + ", isSecretary=" + isSecretary + ", isAuditor=" + isAuditor + ", isTimeKeeper="
                + isTimeKeeper + ", isMiscellaneous=" + isMiscellaneous + ", roleName=" + roleName + ", version="
                + version + "]";
    }



    // -------------------------------
    // add your business methodes here
    // -------------------------------

    public Role() {
        this.isAdmin = false;
        this.isBoardMember = false;
        this.isAuditor = false;
        this.isTimeKeeper = false;
        this.isMiscellaneous = false;
        this.isTreasurer = false;
        this.isSecretary = false;
    }
}
