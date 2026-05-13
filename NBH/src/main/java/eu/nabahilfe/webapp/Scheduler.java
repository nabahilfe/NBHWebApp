package eu.nabahilfe.webapp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import eu.nabahilfe.webapp.email.EmailComposer;
import eu.nabahilfe.webapp.email.EmailDetails;
import eu.nabahilfe.webapp.email.EmailService;
import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.members.MemberRepository;
import eu.nabahilfe.webapp.members.Role;
import eu.nabahilfe.webapp.members.RoleRepository;
import eu.nabahilfe.webapp.timecheques.TimeChequeRepository;

@Component
public class Scheduler {

    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);

    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final RoleRepository roleRepository;
    private final EmailComposer emailComposer;
    private final TimeChequeRepository timeChequeRepository;

    public Scheduler(MemberRepository memberRepository, EmailService emailService, 
    		RoleRepository roleRepository, EmailComposer emailComposer, 
    		TimeChequeRepository timeChequeRepository) {
        this.memberRepository = memberRepository;
        this.emailService = emailService;
        this.roleRepository = roleRepository;
        this.emailComposer = emailComposer;
		this.timeChequeRepository = timeChequeRepository;
    }

    @Scheduled(cron = "0 0 11 * * *")
    public void sendTimeChecksToBookEmail() {

        Integer timeChecksToBook = timeChequeRepository.countByAccountedBy_IdIsNullAndAmountGreaterThan(0.0);

        if (timeChecksToBook <= 0) {
            return;
        }

        List<Role> treasurerRoles = roleRepository.findByIsTreasurer(true);
        List<Member> treasurers = new java.util.ArrayList<>();

        for (Role treasurerRole : treasurerRoles) {
            treasurers.addAll(memberRepository.findByRole(treasurerRole));
        }

        for(Member member : treasurers) {
            EmailDetails email = emailComposer.composeTimeChecksToBookEmail(member.getEmail(), member.getName(), timeChecksToBook);
            emailService.sendEmailHtml(email);
            log.debug("Sent email {} to {}", email.getSubject(), email.getRecipient());
        }

    }


}
