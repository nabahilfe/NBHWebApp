package eu.nabahilfe.webapp.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/email")
public class EmailController {

    private static final Logger log = LoggerFactory.getLogger(EmailController.class);

    private final EmailService emailService;


    private String baseUrl;

    public EmailController(EmailService emailService, @Value("${APP_BASE_URL}") String baseUrl) {
        this.emailService = emailService;
        this.baseUrl = baseUrl;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/sendTestEmail")
    public String sendTestEmail() {
        log.info("Sending test email...");

        EmailDetails details =  new EmailDetails(
            "sysadmin.ma@nabahilfe.eu",
            "Test E-Mail von der Nachbarschaftshilfe",
            "<a href=\"" + baseUrl + "\">" + baseUrl + "</a>"
            + "<p></p>"
            + "<p><small>Diese E-Mail wurde automatisch generiert, bitte nicht antworten!</small></p>"
            );
        emailService.sendEmailHtml(details);
        return "redirect:/#";
    }
}
