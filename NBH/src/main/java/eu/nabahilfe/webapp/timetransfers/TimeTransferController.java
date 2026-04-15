package eu.nabahilfe.webapp.timetransfers;

import java.time.LocalDate;
import java.util.Optional;

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

import eu.nabahilfe.webapp.NbhConst;
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

    private static final Logger log = LoggerFactory.getLogger(TimeTransferController.class);

    public TimeTransferController(MemberRepository memberRepository, OfferRepository offerRepository,
            TimeTransferRepository timeTransferRepository, SecurityUtils securityUtils) {
        this.memberRepository = memberRepository;
        this.offerRepository = offerRepository;
        this.timeTransferRepository = timeTransferRepository;
        this.securityUtils = securityUtils;
    }


    // --------------------
    // VIEW (after save)
    // --------------------

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/self-timetransfer")
    String viewSelfTimeTransfer(final Model model, @RequestParam("id") Long id) {

        // For security, only allow access to time transfers where the current user is the fromMember
        TimeTransfer tt = timeTransferRepository.findByIdAndFromMember_Id(id, securityUtils.getCurrentUserId()).orElse(null);
        if (tt == null) {
            return "redirect:/statuscode/403";
        }

        model.addAttribute("tt", tt);
        return "timetransfers/view-self-timetransfer";
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'TIME_KEEPER')")
    @GetMapping("/view")
    String viewTimeTransfer(final Model model, @RequestParam("id") Long id) {
        TimeTransfer tt = timeTransferRepository.findById(id).orElse(null);
        if (tt == null) {
            model.addAttribute("errorMessage", "Zeitübertragung mit ID " + id + " nicht gefunden.");
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
    String addSelfTimeTransfer(final Model model, @RequestParam(name = "fromMemberId", required = false) Long fromMemberId) {

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

        ttf.setUserFromId(fromMemberId);
        ttf.setUserFromName(fromMember.getNameAndAddress());
        ttf.setAccumulatedHours(fromMember.getAccumulatedHours());
        log.debug("Creating new SELF-TimeTransfer, pre-filled form: {}", ttf);

        model.addAttribute("offers", offerRepository.findAllByOrderByCodeAsc());
        model.addAttribute("ttf", ttf);

        return "timetransfers/self-timetransfer";
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'TIME_KEEPER')")
    @GetMapping("/new")
    String addTimeTransfer(final Model model, @RequestParam(name = "fromMemberId", required = false) Long fromMemberId) {

        TimeTransferForm ttf = new TimeTransferForm();
        ttf.setFromself("false");
        log.debug("Adding new TimeTransfer, fromMemberId={}", fromMemberId);

        if (fromMemberId != null) {
            Member fromMember = memberRepository.findById(fromMemberId).orElse(null);
            if (fromMember == null) {
                model.addAttribute("errorMessage", "Mitglied mit ID " + fromMemberId + " nicht gefunden.");
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


    // --------------------
    // SAVE (process form)
    // --------------------

    // FIXME: Prüfe ob das ein Security Problem sein kann, sollte wegen CSRF Token eigentlich nicht sein
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Transactional
    String saveTimeTransfer(final Model model, @ModelAttribute TimeTransferForm ttf,
            RedirectAttributes redirectAttributes) {

        Long userFromId = ttf.userFromId;
        Long userToId = ttf.userToId;
        Integer hours = (ttf.hoursSelected != null && !ttf.hoursSelected.isBlank())
                ? Integer.valueOf(ttf.hoursSelected) : null;
        Long offerId = ttf.offerId;
        LocalDate dateOfService = ttf.getServiceDate();
        String note = ttf.getNote();
        boolean fromself = ttf.isFromself();

        log.debug("Saving TimeTransfer from user {} to user {} of hours {} for offer {} on date {}",
                userFromId, userToId, hours, offerId, dateOfService);

        model.addAttribute("offers", offerRepository.findAllByOrderByCodeAsc());

        Member memberFrom = memberRepository.findById(userFromId).get();
        Member memberTo = memberRepository.findById(userToId).get();

        // validate transfer is not to self
        if (memberFrom.getId().equals(memberTo.getId())) {
            ttf.setUserFromName(memberFrom.getNameAndAddress());
            ttf.setUserToName(memberTo.getNameAndAddress());
            log.debug("\nTransfer from {} to same member, re-displaying form with error.", ttf);
            model.addAttribute("ttf", ttf);
            model.addAttribute("errorMessage", "Leistungsemfänger und Leistungserbringer dürfen nicht identisch sein!");

            if (fromself) return "timetransfers/self-timetransfer";
            return "timetransfers/create-timetransfer";
        }

        // validate sufficient hours
        if (memberFrom.getAccumulatedHours() == null || memberFrom.getAccumulatedHours() < hours) {
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

        // validate category is 900 or 999 if Sozialkonto is involved
        if ((memberFrom.getSalutation() != null && memberFrom.getSalutation().equals(NbhConst.SOZIALKONTO))
                || (memberTo.getSalutation() != null && memberTo.getSalutation().equals(NbhConst.SOZIALKONTO))) {

            Optional<Offer> offer = offerRepository.findById(offerId);
            if (offer.isPresent()) {
                String code = offer.get().getCode();
                if (!code.equals("900") && !code.equals("999")) {
                    log.debug("\nTransfer involves Sozialkonto, category {} is not allowed. Must be 900 or 999", code);
                    ttf.setUserFromName(memberFrom.getNameAndAddress());
                    ttf.setUserToName(memberTo.getNameAndAddress());
                    log.debug("\nTransfer from Sozialkonto {}, re-displaying form with error.", memberFrom);
                    model.addAttribute("ttf", ttf);
                    model.addAttribute("errorMessage", "Bei Sozialkonto muss Kategorie 900 (oder 999) ausgewählt werden!");

                    if (fromself) return "timetransfers/self-timetransfer";
                    return "timetransfers/create-timetransfer";
                }
            }
        }

        // create and save TimeTransfer
        TimeTransfer tt = new TimeTransfer();

        tt.setFromMember(memberFrom);
        tt.setToMember(memberTo);
        tt.setHours(hours);
        tt.setDateOfService(dateOfService);
        tt.setOffer(offerRepository.findById(offerId).get());
        if (note != null && !note.trim().isEmpty()) tt.setNote(note.trim());

        timeTransferRepository.save(tt);

        // adjust hours
        Integer newHoursFrom = memberFrom.getAccumulatedHours() - hours;
        // if hours are zero, set to null so that UI shows empty field and sorts last
        memberFrom.setAccumulatedHours(newHoursFrom.intValue() == 0 ? null : newHoursFrom);
        memberRepository.save(memberFrom);

        Integer newHoursTo = (memberTo.getAccumulatedHours() == null ? hours
                : memberTo.getAccumulatedHours() + hours);
        memberTo.setAccumulatedHours(newHoursTo);
        memberRepository.save(memberTo);

        redirectAttributes.addFlashAttribute("successMessage",
                (hours == 1 ? "Eine Stunde für " : hours + " Stunden für ") + memberTo.getName() + " verbucht.");

        if (fromself) return "redirect:/timetransfers/self-timetransfer?id=" + tt.getId();
        return "redirect:/timetransfers/view?id=" + tt.getId();
    }


}