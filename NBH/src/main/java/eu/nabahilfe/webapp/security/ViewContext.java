/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.security;

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
        return user != null && user.isAdmin();
    }

    public boolean isExecutiveMember() {
        return user != null && user.isExecutiveMember();
    }

    public boolean isTreasurer() {
        return user != null && user.isTreasurer();
    }

    public boolean isAuditor() {
        return user != null && user.isAuditor();
    }

    public boolean isSecretary() {
        return user != null && user.isSecretary();
    }

    public boolean isTimeKeeper() {
        return user != null && user.isTimeKeeper();
    }

    public boolean hasAnyRole(String...roles) {
        return user != null && user.hasAnyRole(roles);
    }

    public boolean hasRole(String role) {
        return user != null && user.hasAnyRole(role);
    }

    public boolean isSystemAdmin() {
        return user != null && user.isSystemAdmin();
    }

    public boolean isSystemMember() {
        return user != null && user.isSystemMember();
    }

}