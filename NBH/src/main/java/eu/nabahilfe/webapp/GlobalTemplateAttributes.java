package eu.nabahilfe.webapp;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import eu.nabahilfe.webapp.security.CsrfHiddenInput;
import eu.nabahilfe.webapp.security.CustomUserDetails;
import eu.nabahilfe.webapp.security.ViewContext;
import jakarta.servlet.http.HttpServletRequest;


@ControllerAdvice
public class GlobalTemplateAttributes {


    @ModelAttribute("ctxt")
    public ViewContext viewContext(@AuthenticationPrincipal CustomUserDetails cud) {
        return new ViewContext(cud != null ? cud : null);
    }


    @ModelAttribute("csrf")
    public CsrfToken csrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }

    @ModelAttribute("csrfHiddenInput")
    public CsrfHiddenInput csrfHiddenInput(HttpServletRequest request) {
        return new CsrfHiddenInput(csrfToken(request));
    }

}
