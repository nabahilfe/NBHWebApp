package eu.nabahilfe.webapp.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.members.Role;

@SuppressWarnings("serial")
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

    public Member getMember() {
        return member;
    }

    public String getFullName() {
        return member.getName();
    }

    // other methods -> true
}
