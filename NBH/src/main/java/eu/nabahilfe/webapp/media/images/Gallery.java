package eu.nabahilfe.webapp.media.images;

import java.time.LocalDateTime;
import java.util.Objects;
import java.time.LocalDate;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

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

import eu.nabahilfe.webapp.members.Member;


/**
 * Foto- oder Bildergalerie
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "GALLERIES")
public class Gallery  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate galleryDate;    // use it only for images from one specific date, eg. Flohmarkt

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate showGalleryFrom;    // Starting Date for displaying Gallery to Public - if null, do not display

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate showGalleryTo;    // if set, do not show after this date

    @Size(max = 250)
    @NotEmpty
    private String description;

    @Size(max = 250)
    private String remark;    // Anmerkung bzw. Beschreibung, wird in der Galerie-Ansicht angezeigt

    @Column(nullable = false)
    private Boolean isPublic;    // wenn nicht public dann nur für angemeldete Mitglieder sichtbar

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "from_member_id")
    private Member fromMember;    // Bildergalerie eines Mitglieds - das Mitglied kann so eine Galerie anlegen und bearbeiten

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getGalleryDate() {
		return galleryDate;
	}

	public void setGalleryDate(LocalDate galleryDate) {
		this.galleryDate = galleryDate;
	}

	public LocalDate getShowGalleryFrom() {
		return showGalleryFrom;
	}

	public void setShowGalleryFrom(LocalDate showGalleryFrom) {
		this.showGalleryFrom = showGalleryFrom;
	}

	public LocalDate getShowGalleryTo() {
		return showGalleryTo;
	}

	public void setShowGalleryTo(LocalDate showGalleryTo) {
		this.showGalleryTo = showGalleryTo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public Member getFromMember() {
		return fromMember;
	}

	public void setFromMember(Member fromMember) {
		this.fromMember = fromMember;
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
		Gallery other = (Gallery) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Gallery [id=" + id + ", galleryDate=" + galleryDate + ", showGalleryFrom=" + showGalleryFrom
				+ ", showGalleryTo=" + showGalleryTo + ", description=" + description + ", remark=" + remark
				+ ", isPublic=" + isPublic + ", fromMember=" + fromMember + ", createdAt=" + createdAt + ", createdBy="
				+ createdBy + ", updatedAt=" + updatedAt + ", updatedBy=" + updatedBy + ", version=" + version + "]";
	}



    // ------------------------------
    // add your business methods here
    // ------------------------------



}

