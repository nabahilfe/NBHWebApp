/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.registration;

import java.time.LocalDateTime;

import eu.nabahilfe.webapp.GlobalAuditListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * Einmal-Codes für die Registrierung mit E-Mail und Code.
 */
@Entity
@EntityListeners(GlobalAuditListener.class)
@Table(name = "REGISTRATION_CODES")
public class RegistrationCode  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 10)
    @NotEmpty
    private String code;    // Zufällige 6-stellige Zahl

    @Size(max = 80)
    @NotEmpty
    private String email;    // E-Mail zum Code

    @Column(nullable = false)
    private LocalDateTime expiresAt;    // Gültigkeitsdauer des Codes

    // Creation timestamp, value is set by Postgres (see Table definition)
    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Version
    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false)
    private Integer failedAttempts = 0; // number of failed confirmation attempts for this code


    // getters/setters

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public Integer getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(Integer failedAttempts) {
        this.failedAttempts = failedAttempts;
    }


    // toString

    @Override
    public String toString() {
        return "RegistrationCode [id=" + id + ", code=" + code + ", email=" + email + ", expiresAt=" + expiresAt
                + ", createdAt=" + createdAt + ", version=" + version + ", failedAttempts=" + failedAttempts + "]";
    }



    // ------------------------------------------------------------------------
    // generate equals/hashCode methodes with Eclipse here - use ONLY id field!
    // ------------------------------------------------------------------------



    // ------------------------------
    // add your business methods here
    // ------------------------------



}