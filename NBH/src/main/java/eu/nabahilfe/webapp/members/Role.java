package eu.nabahilfe.webapp.members;

import java.util.Objects;

import eu.nabahilfe.webapp.NbhConst;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, name = "is_admin", columnDefinition = "Boolean default false")
    Boolean isAdmin;

    @Column(nullable = false, name = "is_board_member", columnDefinition = "Boolean default false")
    Boolean isBoardMember;

    @Column(nullable = false, name = "is_treasurer", columnDefinition = "Boolean default false")
    Boolean isTreasurer;

    @Column(nullable = false, name = "is_secretary", columnDefinition = "Boolean default false")
    Boolean isSecretary;

    @Column(nullable = false, name = "is_auditor", columnDefinition = "Boolean default false")
    Boolean isAuditor;

    @Column(nullable = false, name = "is_time_keeper", columnDefinition = "Boolean default false")
    Boolean isTimeKeeper;

    @Column(nullable = false, name = "is_miscellaneous", columnDefinition = "Boolean default false")
    Boolean isMiscellaneous;


    @Column(nullable = false, name = "role_name")
    @NotBlank(message = "Rollen-Name darf nicht leer sein!")
    @Size(min = 5, max = NbhConst.MAX_LEN_NAME)
    public String roleName;

    @Version
    @Column(nullable = false)
    Integer version;


    public Role() {
        this.isAdmin = false;
        this.isBoardMember = false;
        this.isAuditor = false;
        this.isTimeKeeper = false;
        this.isMiscellaneous = false;
        this.isTreasurer = false;
        this.isSecretary = false;
    }

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

}
