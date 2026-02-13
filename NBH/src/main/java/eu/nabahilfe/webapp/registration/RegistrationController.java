package eu.nabahilfe.webapp.registration;

import java.time.LocalDateTime;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import eu.nabahilfe.webapp.NbhConst;
import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.members.MemberRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/registration")
@SessionAttributes("registrationSession")
public class RegistrationController {

    private final MemberRepository memberRepository;
    private final RegistrationCodeRepository registrationCodeRepository;

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);


    @ModelAttribute("registrationSession")
    public RegistrationSession registrationSession() {
        return new RegistrationSession();
    }


    public RegistrationController(MemberRepository memberRepository, RegistrationCodeRepository registrationCodeRepository) {
        this.memberRepository = memberRepository;
        this.registrationCodeRepository = registrationCodeRepository;
    }


    @GetMapping("/email")
    public String showEmailForm(@Valid @ModelAttribute("email") String email, BindingResult binding,
            @ModelAttribute("registrationSession") RegistrationSession session ) {

        return "registration/email";
    }



    @PostMapping("/email")
    public String processEmail(Model model, @Valid @RequestParam("email") String email,
            @ModelAttribute("registrationSession") RegistrationSession session ) {

        email = email.trim().toLowerCase();

        Member existing = memberRepository.findByEmail(email);
        if (existing == null) {
            model.addAttribute("errorMessage", "E-Mail '" + email + "' ist nicht bekannt");
            return "registration/email";
        }

        String code = randomCode();
        sendCode(email, code);

        RegistrationCode registrationCode = new RegistrationCode();
        registrationCode.setEmail(email);
        registrationCode.setCode(code);
        registrationCode.setExpiresAt(LocalDateTime.now().plusMinutes(NbhConst.REGISTRATION_CODE_TTL)); // Code ist 15 Minuten gültig

        registrationCodeRepository.save(registrationCode);

        session.start(email); // RESET + Start

        return "redirect:/registration/confirm";
    }



    @GetMapping("/confirm")
    public String showConfirmForm(@ModelAttribute("registrationSession") RegistrationSession session, Model model) {

        if (session.getEmail() == null || session.isExpired()) {
            return "redirect:/registration/email";
        }

        if (session.getStep() != RegistrationStep.EMAIL_VERIFIED) {
            return "redirect:/registration/email";
        }

        RegisterConfirmForm form = new RegisterConfirmForm();
        form.setEmail(session.getEmail());
        model.addAttribute("form", form);

        return "registration/confirm";
    }


    @PostMapping("/confirm")
    @Transactional
    public String processConfirm(Model model, @Valid @ModelAttribute("form") RegisterConfirmForm form, BindingResult binding,
            @ModelAttribute("registrationSession") RegistrationSession session, SessionStatus sessionStatus ) {

        if (session.isExpired() || session.getStep() != RegistrationStep.EMAIL_VERIFIED) {
            sessionStatus.setComplete();
            return "redirect:/registration/email";
        }

        if (binding.hasErrors()) {
            return "registration/confirm";
        }

        if (!verifyCode(session.getEmail(), form.getCode())) {
            model.addAttribute("errorMessage", "Ungültiger oder abgelaufener Code!");
            return "registration/confirm";
        }


        Member member = memberRepository.findByEmail(session.getEmail());
        // FIXME - Implementation missing hashing for password
        member.setPassword(form.getPassword());

        // delete all existing codes for this email
        long deleted = registrationCodeRepository.deleteByEmail(session.getEmail());
        log.debug("Deleted {} registration codes for email {}", deleted, session.getEmail());

        session.complete();
        sessionStatus.setComplete();

        return "redirect:/registration/success";
    }


    @GetMapping("/success")
    public String success() {

        return "registration/success";
    }


    @GetMapping("/login")
    public String showLoginForm() {
        return "registration/login";
    }


    @PostMapping("/login")
    public String processLogin(Model model, @RequestParam("email") String email, @RequestParam("password") String password) {

        if (email != null) {
            email = email.trim().toLowerCase();
        }

        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            model.addAttribute("errorMessage", "E-Mail oder Passwort ist falsch.");
            return "registration/login";
        }

        if (member.getPassword() == null || !member.getPassword().equals(password)) {
            model.addAttribute("errorMessage", "E-Mail oder Passwort ist falsch.");
            return "registration/login";
        }

        model.addAttribute("successMessage", "Erfolgreich angemeldet.");
        return "registration/login";
    }


    // Helper methods


    private boolean verifyCode(String email, String code) {

        log.debug("Verifying code {} for email {}", code, email);

        RegistrationCode regCode = registrationCodeRepository.findFirstByEmailOrderByIdDesc(email);

        if (regCode == null) {
            log.debug("No code found for email {}", email);
            return false;
        }

        if (!regCode.getCode().equals(code)) {
            log.debug("Code does not match for email {}", email);
            return false;
        }

        if (regCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.debug("Code expired for email {}", email);
            return false;
        }

        log.debug("Code verified successfully for email {}", email);
        return true;
    }



    private void sendCode(@Valid String email, String randomCode) {
        // FIXME Add final implementation to send the code to the user via email
        log.warn("Generated code {} for email {}", randomCode, email);
    }


    final int min = 100000;
    final int max = 999999;
    final int range = max - min + 1;

    private String randomCode() {
        Random r = new Random();
        return String.valueOf(r.nextInt(range) + min);
    }

}