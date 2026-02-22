package eu.nabahilfe.webapp.org;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.nabahilfe.webapp.textcontent.TextContent;
import eu.nabahilfe.webapp.textcontent.TextContentRepository;
import eu.nabahilfe.webapp.textcontent.TextContentType;

@Controller
@RequestMapping("/homepage")public class HomepageController {

    private final TextContentRepository textRepo;

    public HomepageController(TextContentRepository textRepo) {
        this.textRepo = textRepo;
    }

    @GetMapping
    public String homepage(Model model) {

        Optional<TextContent> tc = null;

        tc = textRepo.findByContentCode(TextContentType.ABOUT_US.toString());
        setModelAttribut("aboutUs", tc, model);

        tc =  textRepo.findByContentCode(TextContentType.CONTACT.toString());
        setModelAttribut("contact", tc, model);

        tc = textRepo.findByContentCode(TextContentType.EVENTS.toString());
        setModelAttribut("events", tc, model);

        tc = textRepo.findByContentCode(TextContentType.NEWS.toString());
        setModelAttribut("news", tc, model);

        tc = textRepo.findByContentCode(TextContentType.FAQ.toString());
        setModelAttribut("faq", tc, model);

        tc = textRepo.findByContentCode(TextContentType.TERMS_OF_SERVICE.toString());
        setModelAttribut("termsOfService", tc, model);

        tc = textRepo.findByContentCode(TextContentType.PRIVACY_POLICY.toString());
        setModelAttribut("privacyPolicy", tc, model);

        tc = textRepo.findByContentCode(TextContentType.LEGAL_NOTICE.toString());
        setModelAttribut("legalNotice", tc, model);

        return "home";
    }

    private void setModelAttribut(String attr, Optional<TextContent> tc, Model model) {
        if (tc.isPresent()) {
            model.addAttribute(attr, tc.get().getHtmlText());
        } else {
            model.addAttribute(attr, "");
        }
    }


}
