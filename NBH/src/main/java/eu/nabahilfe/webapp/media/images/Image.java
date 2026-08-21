package eu.nabahilfe.webapp.media.images;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import eu.nabahilfe.webapp.members.Member;

import jakarta.persistence.Basic;
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
    private String fileName;

    @Column(nullable = false)
    private Integer imageWidth;

    @Column(nullable = false)
    private Integer imageHeight;

    @Column(nullable = false)
    private Integer imageSize;

    @Column(nullable = false)
    @Basic(fetch = FetchType.LAZY)
    @JdbcTypeCode(SqlTypes.LONGVARBINARY)
    private byte[] image;    // max 1920 x 1920 - scale down at upload

    @Column(nullable = false)
    @Basic(fetch = FetchType.LAZY)
    @JdbcTypeCode(SqlTypes.LONGVARBINARY)
    private byte[] thumbnail;    // max 400 x 400 - scale down at upload

    @Size(max = 20)
    @NotEmpty
    private String contentType;    // image/jpeg, image/png ...

    @Size(max = 250)
    private String description;

    @Column(nullable = false)
    private Boolean isGalleryCover;    // Das repräsentative Bild für die Galerie das in der Galerieübersicht angezeigt wird

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gallery_id", nullable = false)
    private Gallery gallery;

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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(Integer imageWidth) {
		this.imageWidth = imageWidth;
	}

	public Integer getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(Integer imageHeight) {
		this.imageHeight = imageHeight;
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

	public Gallery getGallery() {
		return gallery;
	}

	public void setGallery(Gallery gallery) {
		this.gallery = gallery;
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


    // -------------------------------------------------
    // generate setter/getter methodes with Eclipse here
    // -------------------------------------------------



    // ------------------------------------------------------------------------
    // generate equals/hashCode methodes with Eclipse here - use ONLY id field!
    // ------------------------------------------------------------------------



    // -----------------------------------------------
    // Don't forget to generate toString() for logging
    // -----------------------------------------------



    // ------------------------------
    // add your business methods here
    // ------------------------------



}
