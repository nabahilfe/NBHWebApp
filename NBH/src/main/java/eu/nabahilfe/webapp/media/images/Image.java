package eu.nabahilfe.webapp.media.images;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
 * Bild zu einer Galerie
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "IMAGES")
public class Image  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 250)
    @NotEmpty
    private String imageName;

    @Column(nullable = false)
    private Integer imageSize;

    @Column(nullable = false)
    private byte[] image;

    @Column(nullable = false)
    private byte[] thumbnail;

    @Size(max = 20)
    @NotEmpty
    private String contentType;    // image/jpeg, image/png ...

    @Size(max = 250)
    private String description;

    @Column(nullable = false)
    private Boolean isGalleryCover;    // Das repräsentative Bild für die Galerie das in der Galerieübersicht angezeigt wird

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "belongs_to_id")
    private ImageGallery belongsTo;

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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Integer getImageSize() {
        return imageSize;
    }

    public void setImageSize(Integer imageSize) {
        this.imageSize = imageSize;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsGalleryCover() {
        return isGalleryCover;
    }

    public void setIsGalleryCover(Boolean isGalleryCover) {
        this.isGalleryCover = isGalleryCover;
    }

    public ImageGallery getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(ImageGallery belongsTo) {
        this.belongsTo = belongsTo;
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
        Image other = (Image) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Image [id=" + id + ", imageName=" + imageName + ", imageSize=" + imageSize + ", contentType=" + contentType
                + ", description=" + description + ", isGalleryCover=" + isGalleryCover + ", belongsTo=" + belongsTo
                + ", createdAt=" + createdAt + ", createdBy=" + createdBy + ", updatedAt=" + updatedAt + ", updatedBy="
                + updatedBy + ", version=" + version + "]";
    }

    // ------------------------------
    // add your business methods here
    // ------------------------------



}
