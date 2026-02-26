package eu.nabahilfe.webapp.homepage;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.members.MemberRepository;
import eu.nabahilfe.webapp.security.SecurityUtils;
import eu.nabahilfe.webapp.textcontent.TextContent;
import eu.nabahilfe.webapp.textcontent.TextContentRepository;
import eu.nabahilfe.webapp.textcontent.TextContentType;

// name of my mac: imac.internal


@Controller
public class LegealAndPrivacyController {

    private final TextContentRepository textRepo;
    private final MemberRepository memberRepository;

    private static final Logger log = LoggerFactory.getLogger(LegealAndPrivacyController.class);


    public LegealAndPrivacyController(TextContentRepository textRepo, MemberRepository memberRepository) {
        this.textRepo = textRepo;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/legal_and_privacy")
    public String home(Model model) {

        Optional<TextContent> tc = null;

        tc = textRepo.findByContentCode(TextContentType.PRIVACY_POLICY.toString());
        setModelAttribut("privacyPolicy", tc, model);

        tc = textRepo.findByContentCode(TextContentType.LEGAL_NOTICE.toString());
        setModelAttribut("legalNotice", tc, model);

        List<Member> boardMembers = memberRepository.findBoardMembers();
        model.addAttribute("boardMembers", boardMembers);

        return "legal-and-privacy";
    }


    private void setModelAttribut(String attr, Optional<TextContent> tc, Model model) {
        if (tc.isPresent())
            model.addAttribute(attr, tc.get().getHtmlText());
        else
            model.addAttribute(attr, "");
    }

}
