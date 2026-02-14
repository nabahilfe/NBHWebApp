package eu.nabahilfe.webapp.homepage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import eu.nabahilfe.webapp.security.CustomUserDetails;
import eu.nabahilfe.webapp.security.SecurityUtils;

// name of my mac: imac.internal


@Controller
public class HomePageController {


    @Autowired
    private SecurityUtils securityUtils;

    private static final Logger log = LoggerFactory.getLogger(HomePageController.class);

    @GetMapping("/")
    public String home(Model model) {

        Long currentUserId = securityUtils.getCurrentUserId();
        String currentUsername = securityUtils.getCurrentUsername();

        model.addAttribute("username", currentUsername != null ? currentUsername : "Gast");
        return "home";
    }

}
