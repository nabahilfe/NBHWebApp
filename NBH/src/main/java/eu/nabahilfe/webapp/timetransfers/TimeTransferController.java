/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.timetransfers;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import eu.nabahilfe.webapp.email.EmailComposer;
import eu.nabahilfe.webapp.email.EmailService;
import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.members.MemberRepository;
import eu.nabahilfe.webapp.org.Offer;
import eu.nabahilfe.webapp.org.OfferRepository;
import eu.nabahilfe.webapp.security.SecurityUtils;
import jakarta.transaction.Transactional;



/**
 * Controller for TimeTransfer operations from one Member to another.
 * Has to check hour balances and update Member hours accordingly.
 *
 * No IDs are used in any URL path. All parameters are exchanged via
 * {@link TimeTransferForm} (POST) or query parameters (GET).
 */

@Controller
@RequestMapping("/timetransfers")
public class TimeTransferController {

    private final MemberRepository memberRepository;
    private final OfferRepository offerRepository;
    private final TimeTransferRepository timeTransferRepository;
    private final SecurityUtils securityUtils;

    private final EmailService emailService;
    private final EmailComposer emailComposer;


    private static final Logger log = LoggerFactory.getLogger(TimeTransferController.class);

    public TimeTransferController(MemberRepository memberRepository, OfferRepository offerRepository,
            TimeTransferRepository timeTransferRepository, SecurityUtils securityUtils, EmailComposer emailComposer, EmailService emailService) {
        this.memberRepository = memberRepository;
        this.offerRepository = offerRepository;
        this.timeTransferRepository = timeTransferRepository;
        this.securityUtils = securityUtils;
        this.emailService = emailService;
        this.emailComposer = emailComposer;
    }


    // --------------------
    // VIEW (after save)
    // --------------------

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/self-timetransfer")
    String viewSelfTimeTransfer(final Model model, @RequestParam Long id) {

        // For security, only allow access to time transfers where the current user is the fromMember
        TimeTransfer tt = timeTransferRepository.findByIdAndFromMember_Id(id, securityUtils.getCurrentUserId()).orElse(null);
        if (tt == null) {
            return "redirect:/statuscode/403";
        }

        model.addAttribute("tt", tt);
        return "timetransfers/view-self-timetransfer";
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'TIME_KEEPER', 'BOARD_MEMBER')")
    @GetMapping("/view")
    String viewTimeTransfer(final Model model, @RequestParam Long id) {
        TimeTransfer tt = timeTransferRepository.findById(id).orElse(null);
        if (tt == null) {
            model.addAttribute("status", 404);
            model.addAttribute("error", "Not Found");
            model.addAttribute("message", "Zeitübertragung mit ID " + id + " nicht gefunden.");
            return "error";
        }

        model.addAttribute("tt", tt);
        return "timetransfers/view-timetransfer";
    }


    // --------------------
    // CREATE NEW (show form)
    // --------------------

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/fromself")
    String addSelfTimeTransfer(final Model model, @RequestParam(required = false) Long fromMemberId) {

        TimeTransferForm ttf = new TimeTransferForm();
        ttf.setFromself("true");
        log.debug("Adding new SELF-TimeTransfer, fromMemberId={}", fromMemberId);

        // Security: if a fromMemberId is supplied, ensure the current session user matches it
        if (fromMemberId != null) {
            if (!securityUtils.isAuthenticated()) {
                model.addAttribute("errorMessage", "Nicht authentifiziert.");
                return "redirect:/login";
            }

            if (!securityUtils.isAuthenticatedAndMatches(fromMemberId)) {
                String email = securityUtils.getCurrentUser() == null ? "anonymous" : securityUtils.getCurrentUser().getEmail();
                log.warn("Unauthorized attempt to open self-time-transfer form for memberId {} by user {}", fromMemberId, email);
                return "redirect:/statuscode/403";
            }
        }

        Member fromMember = memberRepository.findById(fromMemberId).orElse(null);

        if (fromMember == null) {
            model.addAttribute("status", 404);
            model.addAttribute("error", "Not Found");
            model.addAttribute("message", "Mitglied mit ID " + fromMemberId + " wurde nicht gefunden.");
            return "error";
        }

        if (fromMember.isSystemAdmin()) {
            throw new IllegalCallerException("System Administratoren können keine Zeitschecks übergeben.");
        }

        ttf.setUserFromId(fromMemberId);
        ttf.setUserFromName(fromMember.getNameAndAddress());
        ttf.setAccumulatedHours(fromMember.getAccumulatedHours());
        log.debug("Creating new SELF-TimeTransfer, pre-filled form: {}", ttf);

        model.addAttribute("offers", offerRepository.findAllByOrderByCodeAsc());
        model.addAttribute("ttf", ttf);

        return "timetransfers/self-timetransfer";
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'TIME_KEEPER', 'BOARD_MEMBER')")
    @GetMapping("/new")
    String addTimeTransfer(final Model model, @RequestParam(required = false) Long fromMemberId) {

        TimeTransferForm ttf = new TimeTransferForm();
        ttf.setFromself("false");
        log.debug("Adding new TimeTransfer, fromMemberId={}", fromMemberId);

        if (fromMemberId != null) {
            Member fromMember = memberRepository.findById(fromMemberId).orElse(null);
            if (fromMember == null) {
                model.addAttribute("status", 404);
                model.addAttribute("error", "Not Found");
                model.addAttribute("message", "Mitglied mit ID " + fromMemberId + " nicht gefunden.");
                return "error";
            }
            ttf.setUserFromId(fromMemberId);
            ttf.setUserFromName(fromMember.getNameAndAddress());
            log.debug("Creating new TimeTransfer, pre-filled form: {}", ttf);
        }

        model.addAttribute("offers", offerRepository.findAllByOrderByCodeAsc());
        model.addAttribute("ttf", ttf);

        return "timetransfers/create-timetransfer";
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'TIME_KEEPER', 'BOARD_MEMBER')")
    @GetMapping("/duplicate")
    String duplicateTimeTransfer(final Model model, @RequestParam Long id) {

        TimeTransfer tt = timeTransferRepository.findById(id).orElse(null);
        if (tt == null) {
            model.addAttribute("status", 404);
            model.addAttribute("error", "Not Found");
            model.addAttribute("message", "Zeitübertragung mit ID " + id + " nicht gefunden.");
            return "error";
        }

        // Pre-fill the create form with all data from the existing TimeTransfer,
        // except the service date, which must be entered again.
        TimeTransferForm ttf = new TimeTransferForm();
        ttf.setFromself("false");
        ttf.setUserFromId(tt.getFromMember().getId());
        ttf.setUserFromName(tt.getFromMember().getNameAndAddress());
        ttf.setUserToId(tt.getToMember().getId());
        ttf.setUserToName(tt.getToMember().getNameAndAddress());
        ttf.setOfferId(tt.getOffer().getId());
        ttf.setHoursSelected(tt.getHours() == null ? null : String.valueOf(tt.getHours()));
        ttf.setNote(tt.getNote());
        // serviceDate intentionally left empty

        log.debug("Duplicating TimeTransfer {} into new form: {}", id, ttf);

        model.addAttribute("offers", offerRepository.findAllByOrderByCodeAsc());
        model.addAttribute("ttf", ttf);

        return "timetransfers/create-timetransfer";
    }


    // --------------------
    // SAVE (process form)
    // --------------------

    @PreAuthorize("hasRole('USER')")
    @Transactional(rollbackOn = Exception.class)
    @PostMapping("/fromself")
    String saveSelfTimeTransfer(final Model model, @ModelAttribute TimeTransferForm ttf,
            RedirectAttributes redirectAttributes) {
        return saveTimeTransferInternal(model, ttf, redirectAttributes, true);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'TIME_KEEPER', 'BOARD_MEMBER')")
    @Transactional(rollbackOn = Exception.class)
    @PostMapping("/staff")
    String saveStaffTimeTransfer(final Model model, @ModelAttribute TimeTransferForm ttf,
            RedirectAttributes redirectAttributes) {
        return saveTimeTransferInternal(model, ttf, redirectAttributes, false);
    }


    private String saveTimeTransferInternal(final Model model, TimeTransferForm ttf,
            RedirectAttributes redirectAttributes, boolean selfFlow) {

        Long userFromId = ttf.userFromId;
        Long userToId = ttf.userToId;
        Integer hours = parseHours(ttf.hoursSelected);
        Long offerId = ttf.offerId;
        LocalDate dateOfService = ttf.getServiceDate();
        String note = ttf.getNote();

        log.debug("Preparing {} TimeTransfer from user {} to user {} of hours {} for offer {} on date {}",
                selfFlow ? "SELF" : "STAFF", userFromId, userToId, hours, offerId, dateOfService);

        model.addAttribute("offers", offerRepository.findAllByOrderByCodeAsc());

        if (userFromId == null || userToId == null || offerId == null || hours == null || hours < 1 || hours > 5
                || dateOfService == null) {
            model.addAttribute("ttf", ttf);
            model.addAttribute("errorMessage", "Ungültige Eingabedaten. Bitte alle Pflichtfelder korrekt ausfüllen.");
            return selfFlow ? "timetransfers/self-timetransfer" : "timetransfers/create-timetransfer";
        }

        // IDOR protection: self flow may only transfer from currently authenticated member.
        if (selfFlow && !securityUtils.isAuthenticatedAndMatches(userFromId)) {
            Long currentUserId = securityUtils.getCurrentUserId();
            log.warn("Blocked self TimeTransfer IDOR attempt: currentUserId={}, requestedFromMemberId={}", currentUserId, userFromId);
            return "redirect:/statuscode/403";
        }

        Member memberFrom = memberRepository.findById(userFromId).orElse(null);
        Member memberTo = memberRepository.findById(userToId).orElse(null);
        Offer offer = offerRepository.findById(offerId).orElse(null);

        if (memberFrom == null || memberTo == null || offer == null) {
            model.addAttribute("ttf", ttf);
            model.addAttribute("errorMessage", "Ungültige Auswahl. Bitte Mitglied/Kategorie erneut auswählen.");
            return selfFlow ? "timetransfers/self-timetransfer" : "timetransfers/create-timetransfer";
        }

        // validate transfer is not to self
        if (memberFrom.getId().equals(memberTo.getId())) {
            return errorTransferToSelf(model, ttf, selfFlow, memberFrom, memberTo);
        }

        // validate receiver is not system admin
        if (memberTo.isSystemAdmin() || memberFrom.isSystemAdmin()) {
            return errorNoTimeChequesWithSysAdmin(model, ttf, selfFlow, memberFrom, memberTo);
        }

        // validate sufficient hours
        if (memberFrom.getAccumulatedHours() == null || memberFrom.getAccumulatedHours() < hours) {
            return errorNotSufficentHours(model, ttf, selfFlow, memberFrom, memberTo);
        }

        // validate category is 950 or 999 if Sozialkonto is involved
        if (memberFrom.isSozialkonto() || memberTo.isSozialkonto()) {
            String code = offer.getCode();
            if (!"950".equals(code) && !"999".equals(code)) {
                return errorWrongCategorie(model, ttf, selfFlow, memberFrom, memberTo, code);
            }
        }

        // create and save TimeTransfer
        TimeTransfer tt = new TimeTransfer();
        tt.setFromMember(memberFrom);
        tt.setToMember(memberTo);
        tt.setHours(hours);
        tt.setDateOfService(dateOfService);
        tt.setOffer(offer);
        if (note != null && !note.trim().isEmpty()) {
            tt.setNote(note.trim());
        }

        timeTransferRepository.save(tt);

        // adjust hours
        Integer newHoursFrom = memberFrom.getAccumulatedHours() - hours;
        // if hours are zero, set to null so that UI shows empty field and sorts to last position in table
        memberFrom.setAccumulatedHours(newHoursFrom == 0 ? null : newHoursFrom);
        memberRepository.save(memberFrom);

        Integer newHoursTo = (memberTo.getAccumulatedHours() == null ? hours
                : memberTo.getAccumulatedHours() + hours);
        memberTo.setAccumulatedHours(newHoursTo);
        memberRepository.save(memberTo);

        sendEmailsToParticipants(hours, offer, note, memberFrom, memberTo, tt);

        redirectAttributes.addFlashAttribute("successMessage",
                (hours == 1 ? "Eine Stunde für " : hours + " Stunden für ") + memberTo.getName() + " verbucht.");

        if (selfFlow) {
            return "redirect:/timetransfers/self-timetransfer?id=" + tt.getId();
        }
        return "redirect:/timetransfers/view?id=" + tt.getId();
    }


    private Integer parseHours(String hoursSelected) {
        if (hoursSelected == null || hoursSelected.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(hoursSelected);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    private String errorWrongCategorie(final Model model, TimeTransferForm ttf, boolean fromself, Member memberFrom,
            Member memberTo, String code) {
        log.debug("\nTransfer involves Sozialkonto, category {} is not allowed. Must be 950 or 999", code);
        ttf.setUserFromName(memberFrom.getNameAndAddress());
        ttf.setUserToName(memberTo.getNameAndAddress());
        log.debug("\nTransfer from Sozialkonto {}, re-displaying form with error.", memberFrom);
        model.addAttribute("ttf", ttf);
        model.addAttribute("errorMessage", "Bei Sozialkonto muss Kategorie 950 (oder 999) ausgewählt werden!");

        if (fromself) return "timetransfers/self-timetransfer";
        return "timetransfers/create-timetransfer";
    }


    private String errorNotSufficentHours(final Model model, TimeTransferForm ttf, boolean fromself,
            Member memberFrom, Member memberTo) {
        ttf.setUserFromName(memberFrom.getNameAndAddress());
        ttf.setUserToName(memberTo.getNameAndAddress());
        log.debug("\nInsufficient hours for member {}, re-displaying form with error.", memberFrom.getName());
        model.addAttribute("ttf", ttf);
        model.addAttribute("errorMessage", memberFrom.getName()
                + " hat nicht genügend Stunden (aktuell "
                + (memberFrom.getAccumulatedHours() == null ? "0" : memberFrom.getAccumulatedHours())
                + " h) für diese Übertragung!");

        if (fromself) return "timetransfers/self-timetransfer";
        return "timetransfers/create-timetransfer";
    }


    private String errorTransferToSelf(final Model model, TimeTransferForm ttf, boolean fromself, Member memberFrom, Member memberTo) {
        ttf.setUserFromName(memberFrom.getNameAndAddress());
        ttf.setUserToName(memberTo.getNameAndAddress());
        log.debug("\nTransfer from {} to same member, re-displaying form with error.", ttf);
        model.addAttribute("ttf", ttf);
        model.addAttribute("errorMessage", "Leistungsempfänger und Leistungserbringer dürfen nicht identisch sein!");

        if (fromself) return "timetransfers/self-timetransfer";
        return "timetransfers/create-timetransfer";
    }


    private String errorNoTimeChequesWithSysAdmin(final Model model, TimeTransferForm ttf, boolean fromself, Member memberFrom, Member memberTo) {
        ttf.setUserFromName(memberFrom.getNameAndAddress());
        ttf.setUserToName(memberTo.getNameAndAddress());
        log.debug("\nTransfer to system admin {}, re-displaying form with error.", memberTo.getName());
        model.addAttribute("ttf", ttf);
        model.addAttribute("errorMessage", "Systemadministratoren können keine Zeitschecks erhalten oder abgeben!");

        if (fromself) return "timetransfers/self-timetransfer";
        return "timetransfers/create-timetransfer";
    }


    private void sendEmailsToParticipants(Integer hours, Offer offer, String note, Member memberFrom, Member memberTo,
            TimeTransfer tt) {
        // email notification to recipient
        if (memberTo.getEmail() != null && !memberTo.getEmail().isBlank()) {
            emailService.sendEmailHtml(emailComposer.composeTimeChequeTransferToEmail(
                    memberTo.getEmail(), memberTo.getEmailSalutation(),
                    memberFrom.getName(),
                    hours, offer.getDescription(), note));
        }
        else {
            log.debug("No email sent for TimeTransfer ID {} because recipient {} has no email address.", tt.getId(), memberTo.getName());
        }

        // email notification to sender
        if (memberFrom.getEmail() != null && !memberFrom.getEmail().isBlank()) {
            // only if sender is not same as creator of transfer, to avoid emails to self
            if (!memberFrom.getId().equals(tt.getCreatedBy().getId())) {
                emailService.sendEmailHtml(emailComposer.composeTimeChequeTransferFromEmail(
                        memberFrom.getEmail(), memberFrom.getEmailSalutation(),
                        memberTo.getName(), tt.getCreatedBy().getName(),
                        hours, offer.getDescription(), note));
            }
        }
        else {
            log.debug("No email sent for TimeTransfer ID {} because sender {} has no email address or is same as creator.", tt.getId(), memberFrom.getName());
        }
    }

}