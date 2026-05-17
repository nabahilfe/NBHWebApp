/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.homepage;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.members.MemberRepository;
import eu.nabahilfe.webapp.textcontent.TextContent;
import eu.nabahilfe.webapp.textcontent.TextContentRepository;
import eu.nabahilfe.webapp.textcontent.TextContentType;

// name of my mac: imac.internal


@Controller
public class HomePageController {

    private final TextContentRepository textRepo;
    private final MemberRepository memberRepository;


    public HomePageController(TextContentRepository textRepo, MemberRepository memberRepository) {
        this.textRepo = textRepo;
        this.memberRepository = memberRepository;
    }

    @GetMapping({"/", "/hompage"})
    public String home(Model model) {

        Optional<TextContent> textContent = null;

        textContent = textRepo.findByContentCode(TextContentType.ABOUT_US.toString());
        setModelAttribut("aboutUs", textContent, model);

        textContent =  textRepo.findByContentCode(TextContentType.CONTACT.toString());
        setModelAttribut("contact", textContent, model);

        textContent = textRepo.findByContentCode(TextContentType.EVENTS.toString());
        setModelAttribut("events", textContent, model);

        textContent = textRepo.findByContentCode(TextContentType.NEWS.toString());
        setModelAttribut("news", textContent, model);

        textContent = textRepo.findByContentCode(TextContentType.FAQ.toString());
        setModelAttribut("faq", textContent, model);

        textContent = textRepo.findByContentCode(TextContentType.TERMS_OF_SERVICE.toString());
        setModelAttribut("termsOfService", textContent, model);

        textContent = textRepo.findByContentCode(TextContentType.PRIVACY_POLICY.toString());
        setModelAttribut("privacyPolicy", textContent, model);

        textContent = textRepo.findByContentCode(TextContentType.LEGAL_NOTICE.toString());
        setModelAttribut("legalNotice", textContent, model);

        List<Member> boardMembers = memberRepository.findBoardMembers();
        model.addAttribute("boardMembers", boardMembers);

        return "home";
    }


    private void setModelAttribut(String attr, Optional<TextContent> tc, Model model) {
        if (tc.isPresent())
            model.addAttribute(attr, tc.get().getHtmlText());
        else
            model.addAttribute(attr, "");
    }


}
