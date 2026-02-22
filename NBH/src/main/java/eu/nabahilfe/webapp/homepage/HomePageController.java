package eu.nabahilfe.webapp.homepage;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import eu.nabahilfe.webapp.security.SecurityUtils;
import eu.nabahilfe.webapp.textcontent.TextContent;
import eu.nabahilfe.webapp.textcontent.TextContentRepository;
import eu.nabahilfe.webapp.textcontent.TextContentType;

// name of my mac: imac.internal


@Controller
public class HomePageController {

    private final TextContentRepository textRepo;
    private final SecurityUtils securityUtils;

    private static final Logger log = LoggerFactory.getLogger(HomePageController.class);


    public HomePageController(TextContentRepository textRepo, SecurityUtils securityUtils) {
        this.textRepo = textRepo;
        this.securityUtils = securityUtils;
    }

    @GetMapping({"/", "/hompage"})
    public String home(Model model) {

        // FIXME - get rid of SecurityUtils and use ctxt
        String currentUsername = securityUtils.getCurrentUsername();

        log.debug("Accessing home page. User: {}, Authenticated: {}", currentUsername, securityUtils.isAuthenticated());

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
        if (tc.isPresent())
            model.addAttribute(attr, tc.get().getHtmlText());
        else
            model.addAttribute(attr, "");
    }


}
