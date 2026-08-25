package eu.nabahilfe.webapp.media.images;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class GalleryForm {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate galleryDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate showGalleryFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate showGalleryTo;

    @Size(max = 250)
    @NotEmpty
    private String description;

    @Size(max = 250)
    private String remark;

    private Boolean isPublic = Boolean.FALSE;

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
}
