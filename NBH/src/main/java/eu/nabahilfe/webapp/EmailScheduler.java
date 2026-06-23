/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import eu.nabahilfe.webapp.accountings.TransactionRepository;
import eu.nabahilfe.webapp.email.EmailComposer;
import eu.nabahilfe.webapp.email.EmailDetails;
import eu.nabahilfe.webapp.email.EmailService;
import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.members.MemberRepository;
import eu.nabahilfe.webapp.members.MembershipFeeRepository;
import eu.nabahilfe.webapp.members.Role;
import eu.nabahilfe.webapp.members.RoleRepository;
import eu.nabahilfe.webapp.timecheques.TimeChequeRepository;

@Component
public class EmailScheduler {

    private static final Logger log = LoggerFactory.getLogger(EmailScheduler.class);

    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final RoleRepository roleRepository;
    private final EmailComposer emailComposer;
    private final TimeChequeRepository timeChequeRepository;
    private final MembershipFeeRepository membershipFeeRepository;
    private final TransactionRepository transactionRepository;

    public EmailScheduler(MemberRepository memberRepository, EmailService emailService,
            RoleRepository roleRepository, EmailComposer emailComposer,
            TimeChequeRepository timeChequeRepository, MembershipFeeRepository membershipFeeRepository,
            TransactionRepository transactionRepository) {
        this.memberRepository = memberRepository;
        this.emailService = emailService;
        this.roleRepository = roleRepository;
        this.emailComposer = emailComposer;
        this.timeChequeRepository = timeChequeRepository;
        this.membershipFeeRepository = membershipFeeRepository;
        this.transactionRepository = transactionRepository;
    }

    @Scheduled(cron = "0 0 3 * * *", zone = "Europe/Vienna") // Run every day at 3:00 AM Vienna time
    public void sendOpenAccontablesToBookEmail() {

        Integer timeChecksToBook = timeChequeRepository.countByAccountedBy_IdIsNullAndAmountGreaterThan(0.0);
        Integer membershipFeesToBook = membershipFeeRepository.countByAccountedBy_IdIsNullAndAmountGreaterThan(0.0);
        long miscBookings = transactionRepository.countUnaccounted();

        log.debug("Found {} time checks and {} membership fees to book", timeChecksToBook, membershipFeesToBook);

        if (timeChecksToBook <= 0 && membershipFeesToBook <= 0 && miscBookings <= 0) {
            return;
        }

        List<Role> treasurerRoles = roleRepository.findByIsTreasurer(true);
        List<Member> treasurers = new java.util.ArrayList<>();

        for (Role treasurerRole : treasurerRoles) {
            treasurers.addAll(memberRepository.findByRole(treasurerRole));
        }

        for(Member treasurer : treasurers) {
            if (treasurer.getEmail() == null || treasurer.getEmail().isBlank()) {
                log.error("Treasurer {} has no email address, skipping email notification", treasurer.getName());
                continue;
            }

            if (timeChecksToBook > 0) {
                EmailDetails email = emailComposer.composeTimeChecksToBookEmail(treasurer.getEmail(),
                        treasurer.getEmailSalutation(), timeChecksToBook);
                emailService.sendEmailHtml(email);
                log.debug("Sent email {} to {}", email.getSubject(), email.getRecipient());
            }

            if (membershipFeesToBook > 0) {
                EmailDetails email = emailComposer.composeMembershipFeesToBookEmail(treasurer.getEmail(),
                        treasurer.getEmailSalutation(), membershipFeesToBook);
                emailService.sendEmailHtml(email);
                log.debug("Sent email {} to {}", email.getSubject(), email.getRecipient());
            }

            if (miscBookings > 0) {
                EmailDetails email = emailComposer.composeMiscBookingsToBookEmail(treasurer.getEmail(),
                        treasurer.getEmailSalutation(), miscBookings);
                emailService.sendEmailHtml(email);
                log.debug("Sent email {} to {}", email.getSubject(), email.getRecipient());
            }
        }

    }


}
