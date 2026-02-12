package eu.nabahilfe.webapp.registration;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RegistrationSession {

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
               createdAt.isBefore(Instant.now().minus(15, ChronoUnit.MINUTES));
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
