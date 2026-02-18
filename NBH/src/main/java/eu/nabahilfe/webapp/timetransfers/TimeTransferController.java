package eu.nabahilfe.webapp.timetransfers;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import eu.nabahilfe.webapp.NbhConst;
import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.members.MemberRepository;
import eu.nabahilfe.webapp.org.Offer;
import eu.nabahilfe.webapp.org.OfferRepository;
import eu.nabahilfe.webapp.security.ViewContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;


/**
 * Controller for TimeTransfer operations from one Member to another.
 * Has to check hour balances and update Member hours accordingly.
 */

@Controller
@RequestMapping("/timetransfers")
public class TimeTransferController {

    private final MemberRepository memberRepository;
    private final OfferRepository offerRepository;
    private final TimeTransferRepository timeTransferRepository;

    private static final Logger log = LoggerFactory.getLogger(TimeTransferController.class);

    public TimeTransferController(MemberRepository memberRepository, OfferRepository offerRepository,
            TimeTransferRepository timeTransferRepository) {
        this.memberRepository = memberRepository;
        this.offerRepository = offerRepository;
        this.timeTransferRepository = timeTransferRepository;
    }


    // --------------------
    // LIST & DETAIL
    // --------------------

    @GetMapping({"/{id}", "/self-timetransfer/{id}"})
    String viewTimeTransfer(final Model model, @PathVariable Long id, HttpServletRequest request) {
        TimeTransfer tt = timeTransferRepository.findById(id).orElse(null);
        if (tt == null) {
            model.addAttribute("errorMessage", "Zeitübertragung mit ID " + id + " nicht gefunden.");
            return "error";
        }
        model.addAttribute("tt", tt);
        String fullUrl = request.getRequestURL().append(request.getQueryString() != null ? "?" + request.getQueryString() : "").toString();
        if (fullUrl.contains("self-timetransfer")) {
            return "timetransfers/view-self-timetransfer";
        }
        return "timetransfers/view-timetransfer";
    }


    // --------------------
    // CREATE NEW
    // --------------------

    @GetMapping(value = {"fromself/{fromMemberId}", "/new", "/new/{fromMemberId}"})
    String addTimeTransfer(final Model model, @PathVariable(required = false) Long fromMemberId, HttpServletRequest request) {

        String fullUrl = request.getRequestURL().append(request.getQueryString() != null ? "?" + request.getQueryString() : "").toString();
        log.debug("Full request URL: {}", fullUrl);

        TimeTransferForm ttf = new TimeTransferForm();;
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

        if (fullUrl.contains("fromself")) {
            return "timetransfers/self-timetransfer";
        }
        return "timetransfers/create-timetransfer";
    }


    @PostMapping
    @Transactional
    String saveTimeTransfer(final Model model, @RequestParam Long userFromId, @RequestParam Long userToId,
            @RequestParam Integer hours, @RequestParam Long offerId, @RequestParam LocalDate dateOfService,
            @RequestParam String fromself, RedirectAttributes redirectAttributes) {

        log.debug("Saving TimeTransfer from user {} to user {} of hours {} for offer {} on date {}",
                userFromId, userToId, hours, offerId, dateOfService);

        model.addAttribute("offers", offerRepository.findAllByOrderByCodeAsc());

        Member memberFrom = memberRepository.findById(userFromId).get();
        Member memberTo = memberRepository.findById(userToId).get();

        // validate transfer is not to self
        if (memberFrom.getId() == memberTo.getId()) {
            // create form with submitted values to re-display form with error message
            TimeTransferForm ttf = createTimeTransferForm(userFromId, userToId, hours, offerId, dateOfService, memberFrom, memberTo);
            log.debug("\nTransfer from {} to same member, re-displaying form with error.", ttf);
            model.addAttribute("ttf", ttf);
            model.addAttribute("errorMessage", "Leistungsemfänger und Leistungserbringer dürfen nicht identisch sein!");

            if (fromself.equals("true")) return "timetransfers/self-timetransfer";
            return "timetransfers/detail-timetransfer";
        }


        // validate sufficient hours
        if (memberFrom.getAccumulatedHours() == null || memberFrom.getAccumulatedHours() < hours) {
            // create form with submitted values to re-display form with error message
            TimeTransferForm ttf = createTimeTransferForm(userFromId, userToId, hours, offerId, dateOfService, memberFrom, memberTo);
            log.debug("\nInsufficient hours for member {}, re-displaying form with error.", memberFrom.getName());
            model.addAttribute("ttf", ttf);
            model.addAttribute("errorMessage", memberFrom.getName()
                    + " hat nicht genügend Stunden (aktuell "
                    + (memberFrom.getAccumulatedHours() == null ? "0" : memberFrom.getAccumulatedHours())
                    + " h) für diese Übertragung!");

            if (fromself.equals("true")) return "timetransfers/self-timetransfer";
            return "timetransfers/detail-timetransfer";
        }


        // validate category is 900 or 999 if Sozialkonte is involved
        if ((memberFrom.getSalutation() != null && memberFrom.getSalutation().equals(NbhConst.SOZIALKONTO))
                || (memberTo.getSalutation() != null && memberTo.getSalutation().equals(NbhConst.SOZIALKONTO))) {

            Optional<Offer> offer = offerRepository.findById(offerId);
            if (offer.isPresent()) {
                String code = offer.get().getCode();
                if (!code.equals("900")  && !code.equals("999")) {
                    log.debug("\nTransfer involves Sozialkonto, category {} is not allowed. Must be 900 or 999", code);
                    // create form with submitted values to re-display form with error message
                    TimeTransferForm ttf = createTimeTransferForm(userFromId, userToId, hours, offerId, dateOfService, memberFrom, memberTo);
                    log.debug("\nTransfer from Sozialkonto {}, re-displaying form with error.", memberFrom);
                    model.addAttribute("ttf", ttf);
                    model.addAttribute("errorMessage", "Bei Sozialkonto muss Kategorie 900 (oder 999) ausgewählt werden!");

                    if (fromself.equals("true")) return "timetransfers/self-timetransfer";
                    return "timetransfers/detail-timetransfer";
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
        // tt.setNote();

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

        if (fromself.equals("true")) return "redirect:/timetransfers/self-timetransfer/" + tt.getId();
        return "redirect:/timetransfers/" + tt.getId();
    }


    // --------------------
    // helper methods
    // --------------------

    private TimeTransferForm createTimeTransferForm(Long userFromId, Long userToId, Integer hours,
            Long offerId, LocalDate dateOfService, Member memberFrom, Member memberTo) {
        TimeTransferForm ttf = new TimeTransferForm();

        ttf.setUserFromId(userFromId);
        ttf.setUserFromName(memberFrom.getNameAndAddress());

        ttf.setUserToId(userToId);
        ttf.setUserToName(memberTo.getNameAndAddress());

        ttf.setOfferId(offerId);
        ttf.setHoursSelected(hours.toString());
        ttf.setServiceDate(dateOfService);
        return ttf;
    }



}