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

@Component
public class Scheduler {

    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);

    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final RoleRepository roleRepository;
    private final EmailComposer emailComposer;

    public Scheduler(MemberRepository memberRepository, EmailService emailService, RoleRepository roleRepository, EmailComposer emailComposer) {
        this.memberRepository = memberRepository;
        this.emailService = emailService;
        this.roleRepository = roleRepository;
        this.emailComposer = emailComposer;
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void sendTimeChecksToBookEmail() {

        // FIXME: get this from query parameter or config
        Integer timeChecksToBook = 5;

        if (timeChecksToBook <= 0) {
            return;
        }

        // FIXME: use findByIsTreasurer(true)
        List<Role> treasurerRoles = roleRepository.findByIsAdmin(true);
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
