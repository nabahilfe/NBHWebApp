package eu.nabahilfe.webapp.system.stats;

import java.time.LocalDateTime;
import java.time.LocalDate;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Size;

/**
 * URL / API Aufrufstatistik
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "API_REQUEST_STATISTICS")
public class ApiRequestStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate requestDate; // Datum des Aufrufs

    @Size(max = 10)
    private String httpMethod;

    private String urlPattern;

    private Integer statusCode;

    private Long requestCount;

    // Creation timestamp, value is set by Postgres (see Table definition)
    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    @Column(nullable = false)
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Long requestCount) {
        this.requestCount = requestCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ApiRequestStatistic other = (ApiRequestStatistic) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ApiRequestStatistic [id=" + id + ", requestDate=" + requestDate + ", httpMethod=" + httpMethod
                + ", urlPattern=" + urlPattern + ", statusCode=" + statusCode + ", requestCount=" + requestCount
                + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", version=" + version + "]";
    }

    public ApiRequestStatistic(LocalDate requestDate, @Size(max = 10) String httpMethod, String urlPattern,
            Integer statusCode, Long requestCount) {
        this.requestDate = requestDate;
        this.httpMethod = httpMethod;
        this.urlPattern = urlPattern;
        this.statusCode = statusCode;
        this.requestCount = requestCount;
    }

    protected ApiRequestStatistic() {
    }

    // ------------------------------
    // add your business methods here
    // ------------------------------

    public void addCount(long count) {
        this.requestCount += count;
    }

}
