package eu.nabahilfe.webapp.timecheques;

import java.time.LocalDate;

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

import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.members.MemberRepository;
import eu.nabahilfe.webapp.org.OfferRepository;
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

    @GetMapping("/{id}")
    String viewTimeTransfer(final Model model, @PathVariable Long id) {
        TimeTransfer tt = timeTransferRepository.findById(id).orElse(null);
        if (tt == null) {
            model.addAttribute("errorMessage", "Zeitübertragung mit ID " + id + " nicht gefunden.");
            return "error";
        }
        model.addAttribute("tt", tt);
        return "timetransfers/view-timetransfer";
    }


    // --------------------
    // CREATE NEW
    // --------------------

    @GetMapping("/new")
    String addTimeTransfer(final Model model) {
        model.addAttribute("offers", offerRepository.findAll());
        model.addAttribute("ttf", null);
        return "timetransfers/create-timetransfer";
    }


    @PostMapping
    @Transactional
    String saveTimeTransfer(final Model model, @RequestParam Long userFromId, @RequestParam Long userToId,
            @RequestParam Integer hours, @RequestParam Long offerId, @RequestParam LocalDate dateOfService,
            RedirectAttributes redirectAttributes) {

        log.debug("Saving TimeTransfer from user {} to user {} of hours {} for offer {} on date {}",
                userFromId, userToId, hours, offerId, dateOfService);

        model.addAttribute("offers", offerRepository.findAll());

        Member memberFrom = memberRepository.findById(userFromId).get();
        Member memberTo = memberRepository.findById(userToId).get();

        // validate transfer is not to self
        if (memberFrom.getId() == memberTo.getId()) {
            TimeTransferForm ttf = createTimeTransferForm(userFromId, userToId, hours, offerId, dateOfService, memberFrom, memberTo);

            log.debug("\nTransfer from {} to same member, re-displaying form with error.", ttf);
            model.addAttribute("ttf", ttf);

            model.addAttribute("errorMessage", "Leistungsemfänger und Leistungserbringer dürfen nicht identisch sein!");
            return "timetransfers/detail-timetransfer";
        }

        // validate sufficient hours
        if (memberFrom.getAccumulatedHours() < hours) {
            TimeTransferForm ttf = createTimeTransferForm(userFromId, userToId, hours, offerId, dateOfService, memberFrom, memberTo);
            log.debug("\nInsufficient hours for member {}, re-displaying form with error.", memberFrom.getName());
            model.addAttribute("ttf", ttf);

            model.addAttribute("errorMessage", "Leistungsempfänger " + memberFrom.getName()
                    + " hat nicht genügend Stunden (aktuell " + memberFrom.getAccumulatedHours() + " h) für diese Übertragung!");
            return "returntimetransfers/detail-timetransfer";
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

        Integer newHoursTo = memberTo.getAccumulatedHours() + hours;
        memberTo.setAccumulatedHours(newHoursTo);
        memberRepository.save(memberTo);

        redirectAttributes.addFlashAttribute("successMessage",
                hours + " Stunden für " + memberTo.getName() + " verbucht.");

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
