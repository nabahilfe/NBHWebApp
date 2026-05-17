/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.members.MemberRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepo;

    public CustomUserDetailsService(MemberRepository repo) {
        this.memberRepo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        Member member = memberRepo.findByEmail(username);
        if (member == null)
            throw new UsernameNotFoundException("Email not found");

        return new CustomUserDetails(member);
    }
}
