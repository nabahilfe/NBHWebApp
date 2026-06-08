/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.registration;

import java.time.LocalDateTime;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import eu.nabahilfe.webapp.email.EmailComposer;
import eu.nabahilfe.webapp.email.EmailDetails;
import eu.nabahilfe.webapp.email.EmailService;
import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.members.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/registration")
@SessionAttributes("registrationSession")
public class RegistrationController {

    private final MemberRepository memberRepository;
    private final RegistrationCodeRepository registrationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailComposer emailComposer;
    private final EmailRateLimiter emailRateLimiter;

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);


    @ModelAttribute("registrationSession")
    public RegistrationSession registrationSession() {
        return new RegistrationSession();
    }

    @ModelAttribute("form")
    public RegisterConfirmForm registerConfirmForm(@ModelAttribute("registrationSession") RegistrationSession session) {
        if (session.getEmail() == null || session.isExpired()) {
            return new RegisterConfirmForm(); // leeres Formular, da kein gültiger Session-Status vorhanden
        }
        else if (session.getStep() != RegistrationStep.EMAIL_VERIFIED) {
            return new RegisterConfirmForm(); // leeres Formular, da Session-Status nicht passend
        }
        else {
            RegisterConfirmForm form = new RegisterConfirmForm();
            form.setUsername(session.getEmail());
            return form; // Formular mit vorausgefüllter E-Mail, da gültiger Session-Status vorhanden
        }
    }


    public RegistrationController(MemberRepository memberRepository, RegistrationCodeRepository registrationCodeRepository,
            PasswordEncoder passwordEncoder, EmailService emailService, EmailComposer emailComposer,
            EmailRateLimiter emailRateLimiter) {
        this.memberRepository = memberRepository;
        this.registrationCodeRepository = registrationCodeRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.emailComposer = emailComposer;
        this.emailRateLimiter = emailRateLimiter;
    }


    @GetMapping("/email")
    public String showEmailForm(@Valid @ModelAttribute String email, BindingResult binding,
            @ModelAttribute("registrationSession") RegistrationSession session, HttpServletRequest request) {

        return "registration/email";
    }



    @PostMapping("/email")
    public String processEmail(Model model, @Valid @RequestParam String email,
            @ModelAttribute("registrationSession") RegistrationSession session, HttpServletRequest request) {

        String clientIp = getClientIp(request);

        // Check rate limit before doing anything else
        if (emailRateLimiter.isBlocked(clientIp)) {
            long minutes = emailRateLimiter.blockedMinutesRemaining(clientIp);
            model.addAttribute("errorMessage",
                    "Zu viele Fehlversuche. Bitte " + minutes + " Minute(n) warten und es erneut versuchen.");
            return "registration/email";
        }

        email = email.trim().toLowerCase();

        Member existing = memberRepository.findByEmail(email);
        if (existing == null) {
            emailRateLimiter.recordFailure(clientIp);
            if (emailRateLimiter.isBlocked(clientIp)) {
                model.addAttribute("errorMessage",
                        "E-Mail '" + email + "' ist nicht bekannt. Zu viele Fehlversuche – bitte "
                        + emailRateLimiter.blockedMinutesRemaining(clientIp) + " Minute(n) warten.");
            } else {
                model.addAttribute("errorMessage", "E-Mail '" + email + "' ist nicht bekannt");
            }
            return "registration/email";
        }

        String code = randomCode();
        sendCode(email, existing.getFirstName() + " " + existing.getLastName(), code);

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
        form.setUsername(session.getEmail());
        model.addAttribute("form", form);

        return "registration/confirm";
    }


    @PostMapping("/confirm")
    @Transactional
    public String processConfirm(Model model, @Valid @ModelAttribute RegisterConfirmForm form, BindingResult binding,
            @ModelAttribute("registrationSession") RegistrationSession session, SessionStatus sessionStatus ) {

        if (session.isExpired() || session.getStep() != RegistrationStep.EMAIL_VERIFIED) {
            sessionStatus.setComplete();
            return "redirect:/registration/email";
        }

        if (binding.hasErrors()) {
            return "registration/confirm";
        }

        // fetch latest code for this email
        RegistrationCode regCode = registrationCodeRepository.findFirstByEmailOrderByIdDesc(session.getEmail());

        if (regCode == null) {
            model.addAttribute("errorMessage", "Kein Registrierungscode gefunden. Bitte neu anfordern.");
            sessionStatus.setComplete();
            return "registration/confirm";
        }

        // check code validity
        if (!verifyCode(session.getEmail(), form.getCode())) {
            // increment failed attempts on the found registration code
            try {
                regCode.setFailedAttempts(regCode.getFailedAttempts() + 1);
                registrationCodeRepository.save(regCode);
            } catch (Exception e) {
                log.warn("Could not increment failedAttempts for regCode {}: {}", regCode, e.getMessage());
            }

            final int MAX_ATTEMPTS = 3;
            if (regCode.getFailedAttempts() >= MAX_ATTEMPTS) {
                // delete the code after too many failed attempts
                registrationCodeRepository.deleteByEmail(session.getEmail());
                model.addAttribute("errorMessage", "Zu viele ungültige Versuche. Bitte neuen Code anfordern.");
                sessionStatus.setComplete();
                return "registration/confirm";
            }

            model.addAttribute("errorMessage", "Ungültiger oder abgelaufener Code!");
            return "registration/confirm";
        }

        // code verified successfully - proceed with password set
        Member member = memberRepository.findByEmail(session.getEmail());
        member.setPassword(passwordEncoder.encode(form.getPassword()));

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
    public String showLoginForm(@RequestParam(required = false) String error, Model model, HttpServletRequest request) {
        if ("true".equals(error)) {
            model.addAttribute("errorMessage", "E-Mail oder Passwort falsch!");
        }

        // Restore submitted username from session if present (set by failure handler)
        try {
            var session = request.getSession(false);
            if (session != null) {
                Object lastUser = session.getAttribute("LAST_USERNAME");
                if (lastUser != null) {
                    model.addAttribute("username", lastUser.toString());
                    session.removeAttribute("LAST_USERNAME");
                }
            }
        } catch (Exception e) {
            log.warn("Could not restore last username from session: {}", e.getMessage());
        }

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



    private void sendCode(@Valid String recipient, String name, String randomCode) {
        EmailDetails email = emailComposer.composeConfirmationCodeEmail(recipient, name, randomCode);
        emailService.sendEmailHtml(email);
        log.debug("Generated code ##### {} ##### for email {}", randomCode, email);
    }


    final int min = 100000;
    final int max = 999999;
    final int range = max - min + 1;

    private String randomCode() {
        Random r = new Random();
        return String.valueOf(r.nextInt(range) + min);
    }

    /** Resolves the real client IP, respecting common reverse-proxy headers. */
    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }

}