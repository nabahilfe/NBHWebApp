package eu.nabahilfe.webapp.security;

import eu.nabahilfe.webapp.members.Member;

public class ViewContext {

    // FIXME ? - use CustomUserDetails instead of Member
    private final CustomUserDetails user;

    public ViewContext(CustomUserDetails user) {
        this.user = user;
    }

    public boolean isAuthenticated() {
        return user != null;
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public String getUsername() {
        return user != null ? user.getUsername() : "Gast";
    }

    public String getFullName() {
        return user != null ? user.getName() : "Gast";
    }

    public String getFirstNameLastName() {
        return user != null ? user.getFirstNameLastName() : "Gast";
    }


    public boolean isAdmin() {
        return user != null && user.isAdmin() ? true : false;
    }

    public boolean isBoardMember() {
        return user != null && user.isBoardMember() ? true : false;
    }

    public boolean isTreasurer() {
        return user != null && user.isTreasurer() ? true : false;
    }

    public boolean isAuditor() {
        return user != null && user.isAuditor() ? true : false;
    }

    public boolean isSecretary() {
        return user != null && user.isSecretary() ? true : false;
    }

     public boolean isTimeKeeper() {
        return user != null && user.isTimeKeeper() ? true : false;
    }


}