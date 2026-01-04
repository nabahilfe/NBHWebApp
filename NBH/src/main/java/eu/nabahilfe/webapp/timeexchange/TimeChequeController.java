package eu.nabahilfe.webapp.timeexchange;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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




    // --------------------
    // CREATE NEW
    // --------------------

    // Create TimeCheque for specific Member

    @GetMapping("/new")
    String addTimeCheque(final Model model, @RequestParam Long memberId) {
        log.debug("addTimeCheque for memberId={}", memberId);
        Optional<Member> member = memberRepository.findById(memberId);

        // Business Logic - determine TimeCheque hours based on existing TimeCheques
        TimeCheque tc = null;
        int existingTimeCheques = timeChequeRepository.countByAssignedTo(member.get());
        if (existingTimeCheques == 0) {
            log.debug("Member id={} has no existing TimeCheques, using first hours of {}", memberId, NbhConst.FIRST_TIME_CHEQUE_HOURS);
            tc = createTimeCheque(NbhConst.FIRST_TIME_CHEQUE_HOURS, member.get());
        } else {
            tc = createTimeCheque(NbhConst.REGULAR_TIME_CHEQUE_HOURS, member.get());
            log.debug("Member id={} has {} existing TimeCheques, using regular hours of {}", memberId, existingTimeCheques, NbhConst.REGULAR_TIME_CHEQUE_HOURS);
        }

        model.addAttribute("timeCheque", tc);
        model.addAttribute("purchasedTimeCheques", timeCheckRepository.findLast10ByAssignedToIdOrderByOrderDateDesc(memberId));

        return "timecheques/detail-timecheque";
    }


    @PostMapping
    @Transactional
    String saveTimeCheque(final Model model, @RequestParam LocalDate orderDate) {
        TimeCheque tc = (TimeCheque) model.getAttribute("timeCheque");
        tc.setOrderDate(orderDate);
        timeChequeRepository.save(tc);

        // Update Member's accumulated hours
        Integer newHours = tc.getAssignedTo().getAccumulatedHours() + tc.getHours();
        tc.getAssignedTo().setAccumulatedHours(newHours);
        memberRepository.save(tc.getAssignedTo());

        model.addAttribute("member", tc.getAssignedTo());
        model.addAttribute("successMessage", "Zeitscheck mit " + tc.getHours() + "h wurde hinzugefügt.");
        log.debug("Saved TimeCheque id={} for Member id={}", tc.getId(), tc.getAssignedTo().getId());

        return "timecheques/summary-timecheque";
    }


    private TimeCheque createTimeCheque(int timeChequeHours, Member member) {
        TimeCheque tc = new TimeCheque();

        tc.hours = timeChequeHours;
        // FIXME: Richtigen Betrag aus TC-Kosten Tabell holen
        tc.amount = timeChequeHours <= 5 ? BigDecimal.valueOf(0) : BigDecimal.valueOf(3.60 * timeChequeHours);
        tc.assignedTo = member;
        // FIXME: must be logged in user!
        tc.createdBy = member;

        return tc;
    }


}
