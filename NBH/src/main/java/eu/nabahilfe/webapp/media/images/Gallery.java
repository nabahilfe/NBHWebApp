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
@Table(name = "IMAGE_GALLERY")
public class ImageGallery  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate galleryDate;

    @Size(max = 250)
    @NotEmpty
    private String galleryDescription;

    @Size(max = 250)
    private String galleryRemark;    // Anmerkung für Editor, wird in der Gallery-Ansicht nicht angezeigt

    @Column(nullable = false)
    private Boolean isPublic;    // wenn nicht public dann nur für angemeldete Mitglieder sichtbar

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



    public LocalDate getGalleryDate() {
        return galleryDate;
    }

    public void setGalleryDate(LocalDate galleryDate) {
        this.galleryDate = galleryDate;
    }

    public String getGalleryDescription() {
        return galleryDescription;
    }

    public void setGalleryDescription(String galleryDescription) {
        this.galleryDescription = galleryDescription;
    }

    public String getGalleryRemark() {
        return galleryRemark;
    }

    public void setGalleryRemark(String galleryRemark) {
        this.galleryRemark = galleryRemark;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        ImageGallery other = (ImageGallery) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "ImageGallery [id=" + id + ", galleryDate=" + galleryDate + ", galleryDescription=" + galleryDescription
                + ", galleryRemark=" + galleryRemark + ", isPublic=" + isPublic + ", createdAt=" + createdAt
                + ", createdBy=" + createdBy + ", updatedAt=" + updatedAt + ", updatedBy=" + updatedBy + ", version="
                + version + "]";
    }



    // ------------------------------
    // add your business methods here
    // ------------------------------



}
