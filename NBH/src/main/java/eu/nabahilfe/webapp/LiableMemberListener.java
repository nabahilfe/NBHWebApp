package eu.nabahilfe.webapp;

import eu.nabahilfe.webapp.accountings.Accountable;
import eu.nabahilfe.webapp.accountings.AccountingEntry;
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

        Member current = securityUtils.getCurrentUser();

        if (entity instanceof Accountable accountable) {

	        if (accountable.getLiableMemberName() == null || accountable.getLiableMemberName().isEmpty()) {
		        if (current != null) {
		            accountable.setLiableMemberName(current.getName());
		        }
		        else {
		            // Handle the case where current user is null, if necessary
		            // For example, you might want to throw an exception or log a warning
		            throw new IllegalStateException("Current user is not available for setting liable member name in Accountable.");
		        }
	        }
	        return;
        }


        if (entity instanceof AccountingEntry accEntry) {

	        if (accEntry.getLiableMemberName() == null || accEntry.getLiableMemberName().isEmpty()) {
		        if (current != null) {
		        	accEntry.setLiableMemberName(current.getName());
		        }
		        else {
		            // Handle the case where current user is null, if necessary
		            // For example, you might want to throw an exception or log a warning
		            throw new IllegalStateException("Current user is not available for setting liable member name in AccountingEntry.");
		        }
	        }
	        return;
        }

    }
}