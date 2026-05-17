/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.members;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller für die Suche nach Mitgliedern. Es wird nur die ID und der Name zurück
 * Verwendet z.B. für Zeitscheck Zuweisungen um die Mitglieder zu finden in einem Suche/Dropdown
 */

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/members")
public class MemberSearchController {

    private final MemberRepository memberRepository;

    public MemberSearchController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/search")
    public List<MemberDTO> search(@RequestParam String q) {
        return memberRepository
            .findAllByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(q, q)
            .stream()
            .map(m -> new MemberDTO(m.getId(), m.getNameAndAddress()))
            .toList();
    }

    record MemberDTO(Long id, String nameAndAdress) {

    }
}
