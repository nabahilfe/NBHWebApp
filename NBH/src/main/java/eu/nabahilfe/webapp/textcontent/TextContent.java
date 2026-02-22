package eu.nabahilfe.webapp.textcontent;

import java.time.LocalDateTime;

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
import jakarta.validation.constraints.Size;

import eu.nabahilfe.webapp.members.Member;


/**
 * Texte für die HP. Erfassung als MarkDown, angezeigt wird daraus generiertes HTML
 */
@Entity
@Table(name = "TEXT_CONTENTS")
public class TextContent  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 20)
    private String elementCode;    // Aus ENUM - Für welches Element gilt der Text

    @Size(max = 4000)
    private String mdText;    // Text mit Markdoen formatiert

    @Size(max = 4000)
    private String htmlText;    // Aus dem Markdown Text generierter HTML Text

    // Creation timestamp, value is set by Postgres (see Table definition)
    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // FIXME: im Generator: "@Column(nullable = false)"
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