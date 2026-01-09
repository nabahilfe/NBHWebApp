package eu.nabahilfe.webapp.timecheques;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.SessionAttributes;

import eu.nabahilfe.webapp.NbhConst;
import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.members.MemberRepository;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/timecheques")
@SessionAttributes("timeCheque")
public class TimeChequeController {

    private final TimeCheckRepository timeChequeRepository;
    private final MemberRepository memberRepository;
    private final TimeCheckRepository timeCheckRepository;

    private static final Logger log = LoggerFactory.getLogger(TimeChequeController.class);

    public TimeChequeController(TimeCheckRepository timeChequeRepository, MemberRepository memberRepository,
            TimeCheckRepository timeCheckRepository) {
        this.timeChequeRepository = timeChequeRepository;
        this.memberRepository = memberRepository;
        this.timeCheckRepository = timeCheckRepository;
    }


    // --------------------
    // LIST & DETAIL
    // --------------------


    @GetMapping("/{id}")
    String viewTimeCheque(final Model model, @PathVariable Long id) {

        TimeCheque tc = timeChequeRepository.findById(id).orElse(null);
        if (tc == null) {
            model.addAttribute("errorMessage", "Zeitscheck mit ID " + id + " nicht gefunden.");
            return "error";
        }

        Member member = memberRepository.findById(tc.getAssignedTo().getId()).orElse(null);
        if (member == null) {
            model.addAttribute("errorMessage", "Mitglied mit ID " + id + " nicht gefunden.");
            return "error";
        }

        model.addAttribute("timeCheque", tc);
        model.addAttribute("member", member);
        model.addAttribute("purchasedTimeCheques", timeCheckRepository.
                findAllByAssignedToIdOrderByOrderDateDesc(member.getId()));

        return "timecheques/summary-timecheque";

    }


    @GetMapping("/unaccounted")
    String listUnaccountedTimeCheques(final Model model) {
        log.debug("Listing unaccounted TimeCheques");
        model.addAttribute("timeCheques", timeChequeRepository.findAllByAccountingEntryIsNullOrderByOrderDateAsc());
        log.debug("Found {} unaccounted TimeCheques", ((java.util.List<?>) model.getAttribute("timeCheques")).size());
        return "timecheques/list-timecheques";

    }


    // --------------------
    // CREATE NEW
    // --------------------

    // Create TimeCheque for specific Member

    @GetMapping("/new")
    String addTimeCheque(final Model model, @RequestParam Long memberId) {

        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            model.addAttribute("errorMessage", "Mitglied mit ID " + memberId + " nicht gefunden.");
            return "error";
        }

        // Business Logic: determine TimeCheque hours based on existing TimeCheques
        TimeCheque tc = null;
        int existingTimeCheques = timeChequeRepository.countByAssignedTo(member);
        if (existingTimeCheques == 0) {
            log.debug("Member id={} has no existing TimeCheques, using first hours of {}", memberId, NbhConst.FIRST_TIME_CHEQUE_HOURS);
            tc = createTimeCheque(NbhConst.FIRST_TIME_CHEQUE_HOURS, member);
        } else {
            tc = createTimeCheque(NbhConst.REGULAR_TIME_CHEQUE_HOURS, member);
            log.debug("Member id={} has {} existing TimeCheques, using regular hours of {}", memberId, existingTimeCheques, NbhConst.REGULAR_TIME_CHEQUE_HOURS);
        }

        model.addAttribute("timeCheque", tc);
        model.addAttribute("purchasedTimeCheques", timeCheckRepository.findAllByAssignedToIdOrderByOrderDateDesc(memberId));

        String validationError = validateData(member, tc, existingTimeCheques);
        if (validationError != null) {
            model.addAttribute("errorMessage", validationError);
            log.debug("Validation error for TimeCheque for Member id={}: {}", memberId, validationError);
        }

        return "timecheques/create-timecheque";
    }



    // Save the new TimeCheque and update Member's accumulated hours

    @PostMapping
    @Transactional
    String saveTimeCheque(final Model model, @RequestParam LocalDate orderDate) {

        TimeCheque tc = (TimeCheque) model.getAttribute("timeCheque");
        tc.setOrderDate(orderDate);
        timeChequeRepository.save(tc);

        // BusinessRule: Update Member's accumulated hours
        Integer newHours = tc.getAssignedTo().getAccumulatedHours() + tc.getHours();
        tc.getAssignedTo().setAccumulatedHours(newHours);
        memberRepository.save(tc.getAssignedTo());

        model.addAttribute("successMessage", "Zeitscheck mit " + tc.getHours() + "h wurde hinzugefügt.");

        log.debug("TimeCheque saved: {}", tc);

        return "redirect:/timecheques/" + tc.getId();
    }




    // --------------------
    // helper methods
    // --------------------

    private TimeCheque createTimeCheque(int timeChequeHours, Member member) {

        TimeCheque tc = new TimeCheque();

        tc.hours = timeChequeHours;
        // FIXME: Richtigen Betrag aus TC-Kosten Tabelle holen
        tc.amount = timeChequeHours <= 5 ? BigDecimal.valueOf(0) : BigDecimal.valueOf(3.60 * timeChequeHours);
        tc.assignedTo = member;
        tc.orderDate = LocalDate.now();
        // FIXME: must be logged in user!
        tc.createdBy = member;

        return tc;
    }


    private String validateData(Member member, TimeCheque timeCheque, int existingTimeCheques) {
        // Business Rule: TimeCheques can only be purchased if Member has less than 5 accumulated hours,
        // except for the first TimeCheque, which is free of charge.
        if (member.getAccumulatedHours() >= NbhConst.MIN_HOURS_FOR_TIME_CHEQUE && existingTimeCheques > 0) {
            return "Zeitschecks können erst bei weniger als 5 Stunden Zeitguthaben erworben werden." +
                   " Aktuelles Zeitguthaben: " + member.getAccumulatedHours() + " Stunden.";
        }
        return null;
    }



}
