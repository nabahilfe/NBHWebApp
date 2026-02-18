package eu.nabahilfe.webapp.security;

import eu.nabahilfe.webapp.members.Member;

public class ViewContext {

    // FIXME ? - use CustomUserDetails instead of Member
    private final Member user;

    public ViewContext(Member user) {
        this.user = user;
    }

    public boolean isAuthenticated() {
        return user != null;
    }

    public Member getUser() {
        return user;
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public String getUsername() {
        return user != null ? user.getFirstName() : "Gast";
    }

    public String getFullUsername() {
        return user != null ? user.getName() : "Gast";
    }

    public boolean isAdmin() {
        if (user != null && 3 < 4) return true; // FIXME - remove this when we have real users
        if (user == null || user.getRole() == null) return false;
        return user.getRole().getAuthorities().contains("ROLE_ADMIN");
    }

}