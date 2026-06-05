/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.timecheques;

import java.io.IOException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.members.MemberRepository;
import eu.nabahilfe.webapp.security.SecurityUtils;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class TimeChequePdfController {

    private final MemberRepository memberRepository;
    private final SecurityUtils securityUtils;
    private final TimeChequePdfPersonalizationService pdfPersonalizationService = new TimeChequePdfPersonalizationService();

    public TimeChequePdfController(MemberRepository memberRepository, SecurityUtils securityUtils) {
        this.memberRepository = memberRepository;
        this.securityUtils = securityUtils;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/download/timecheque-pdf")
    public void downloadTimeChequePdf(@RequestParam Long memberId, HttpServletResponse response) throws IOException {

        // Security: only allow download for own data unless ADMIN/TIME_KEEPER
        if (!securityUtils.isAuthenticatedAndMatches(memberId) && !securityUtils.hasAnyRole("ADMIN", "TIME_KEEPER")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String memberName = member.getName();
        String memberNumber = member.getMemberNmbr() != null ? member.getMemberNmbr().toString() : "";

        // Set response headers BEFORE writing to output stream
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"ZeitScheck " + member.getLastName() + " " + member.getFirstName() + ".pdf\"");

        pdfPersonalizationService.personalizeTimeCheque(memberName, memberNumber, response.getOutputStream());
        response.flushBuffer();
    }
}
