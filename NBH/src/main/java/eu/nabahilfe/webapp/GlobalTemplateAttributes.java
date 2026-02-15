package eu.nabahilfe.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import eu.nabahilfe.webapp.security.CustomUserDetails;
import eu.nabahilfe.webapp.security.SecurityUtils;
import eu.nabahilfe.webapp.security.ViewContext;


@ControllerAdvice
public class GlobalTemplateAttributes {

    @Autowired
    private SecurityUtils securityUtils;


    @ModelAttribute("ctxt")
    public ViewContext viewContext(@AuthenticationPrincipal CustomUserDetails cud) {
        return new ViewContext(cud != null ? cud.getMember() : null);
    }
}
