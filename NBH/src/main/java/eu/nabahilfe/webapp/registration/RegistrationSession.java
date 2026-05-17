/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.registration;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import eu.nabahilfe.webapp.NbhConst;

public class RegistrationSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private String email;
    private RegistrationStep step;
    private Instant createdAt;

    public String getEmail() {
        return email;
    }

    public RegistrationStep getStep() {
        return step;
    }

    public boolean isExpired() {
        return createdAt != null &&
            createdAt.isBefore(Instant.now().minus(NbhConst.REGISTRATION_CODE_TTL, ChronoUnit.MINUTES));
    }

    public void start(String email) {
        this.email = email;
        this.step = RegistrationStep.EMAIL_VERIFIED;
        this.createdAt = Instant.now();
    }

    public void complete() {
        this.step = RegistrationStep.COMPLETED;
    }
}
