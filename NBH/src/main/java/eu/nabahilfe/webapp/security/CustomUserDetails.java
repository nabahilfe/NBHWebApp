package eu.nabahilfe.webapp.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.members.Role;

public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private final Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorities = new HashSet<>();

        Role role = member.getRole();

        if (role != null) {
            for (String auth : role.getAuthorities()) {
                authorities.add(new SimpleGrantedAuthority(auth));
            }
        }
        else {
            // if no role is assigned, give the user a default role
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getFirstName();
    }

    @Override
    public boolean isEnabled() {
        return member.isActive();
    }

    // other methods -> true



    public Long getId() {
        return member.getId();
    }

    public String getName() {
        return member.getName();
    }

    public String getFirstNameLastName() {
        return member.getFirstName() + " " + member.getLastName();
    }


    public Member getUser() {
        return member;
    }


    // acces to roles

    public boolean isAdmin() {
        return getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    public boolean isBoardMember() {
        return getAuthorities().contains(new SimpleGrantedAuthority("ROLE_BOARD_MEMBER"));
    }

    public boolean isTreasurer() {
        return getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TREASURER"));
    }

    public boolean isSecretary() {
        return getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SECRETARY"));
    }

    public boolean isAuditor() {
        return getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AUDITOR"));
    }

    public boolean isTimeKeeper() {
        return getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TIME_KEEPER"));
    }


}
