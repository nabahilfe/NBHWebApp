/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import eu.nabahilfe.webapp.members.Member;

/**
 * This is needed to handel authenticated / un-authenticated useres and audit info in entities (createdBy, updatedBy)
 */
@Component
public class SecurityUtils {


    public Member getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getMember();
        }
        return null;
    }


    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getId();
        }
        return null;
    }


    public Boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String);
    }


    public boolean memberIdMatchesCurrentUser(Long memberId) {
        return getCurrentUser() != null && getCurrentUser().getId().equals(memberId);
    }


    /**
     * Returns true if there is an authenticated current user and their id equals the provided id.
     * This centralizes the common "is authenticated and owns resource" check used in controllers.
     */
    public boolean isAuthenticatedAndMatches(Long memberId) {
        Member current = getCurrentUser();
        return current != null && current.getId() != null && current.getId().equals(memberId);
    }


    public boolean hasAnyRole(String... roles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).hasAnyRole(roles);
        }
        return false;
    }

}