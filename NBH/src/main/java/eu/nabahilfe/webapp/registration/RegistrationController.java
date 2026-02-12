package eu.nabahilfe.webapp.registration;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.members.MemberRepository;
import eu.nabahilfe.webapp.members.RoleRepository;
import eu.nabahilfe.webapp.timecheques.TimeChequeRepository;
import eu.nabahilfe.webapp.timetransfers.TimeTransferRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final MemberRepository memberRepository;
    // FIXME private final EmailService emailService;



    public RegistrationController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }






    @PostMapping("/email")
    public String processEmail(@Valid @ModelAttribute("email") String email, BindingResult binding,
            @ModelAttribute("registrationSession") RegistrationSession session ) {

        if (binding.hasErrors()) {
            return "register/email";
        }
// FIXME - Implementation missing

//        if (!emailService.isAllowed(form.getEmail())) {
//            binding.rejectValue("email", "invalid", "E-Mail nicht erlaubt");
//            return "register/email.jte";
//        }
//
//        Member existing = memberRepository.findByEmail(email.trim().toLowerCase());
//        if (existing == null) {
//            binding.rejectValue("email", "non existent", "E-Mail ist nicht bekannt");
//            return "register/email";
//        }
//
//        verificationService.sendCode(form.getEmail());
//
//        session.start(form.getEmail()); // RESET + Start

        return "redirect:/register/confirm";
    }

}
