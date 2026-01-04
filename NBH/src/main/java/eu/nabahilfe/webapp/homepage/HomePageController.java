package eu.nabahilfe.webapp.homepage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// name of my mac: imac.internal


@Controller
public class HomePageController {


    private static final Logger log = LoggerFactory.getLogger(HomePageController.class);

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("username", "John Doe");
        return "home";
    }

}
