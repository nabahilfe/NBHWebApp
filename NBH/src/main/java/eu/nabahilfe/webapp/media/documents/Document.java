package eu.nabahilfe.webapp.media.documents;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import eu.nabahilfe.webapp.members.Member;

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
 * Dokument - PDF erlauben, sonst nix
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "DOCUMENTS")
public class Document  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 250)
    @NotEmpty
    private String fileName;

    @Column(nullable = false)
    private Integer documentSize;

    @Column(nullable = false)
    private byte[] documentDData;

    @Size(max = 20)
    @NotEmpty
    private String contentType;    // application/pdf

    @Size(max = 250)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lobrary_id")
    private DocumentLibrary lobrary;

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
