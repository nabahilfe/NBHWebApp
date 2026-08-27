package eu.nabahilfe.webapp.media.documents;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class LibraryForm {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate showLibraryFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate showLibraryTo;

    @Size(max = 250)
    @NotEmpty
    private String description;

    @Size(max = 250)
    private String remark;

    private Boolean isPublic = Boolean.FALSE;

    public LocalDate getShowLibraryFrom() {
        return showLibraryFrom;
    }

    public void setShowLibraryFrom(LocalDate showLibraryFrom) {
        this.showLibraryFrom = showLibraryFrom;
    }

    public LocalDate getShowLibraryTo() {
        return showLibraryTo;
    }

    public void setShowLibraryTo(LocalDate showLibraryTo) {
        this.showLibraryTo = showLibraryTo;
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
