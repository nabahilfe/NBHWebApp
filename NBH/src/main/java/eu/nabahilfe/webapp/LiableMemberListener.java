package eu.nabahilfe.webapp;

import eu.nabahilfe.webapp.accountings.Accountable;
import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.security.SecurityUtils;
import jakarta.persistence.PrePersist;

public class LiableMemberListener {

    private final SecurityUtils securityUtils;

    public LiableMemberListener(SecurityUtils securityUtils) {
                this.securityUtils = securityUtils;
    }

    @PrePersist
    public void prePersist(Object entity) {

        if (!(entity instanceof Accountable accountable)) {
            return;
        }

        if (accountable.getLiableMemberName() != null) {
            return;
        }

        Member current = securityUtils.getCurrentUser();

        if (current != null) {
            accountable.setLiableMemberName(current.getName());
        }
    }
}