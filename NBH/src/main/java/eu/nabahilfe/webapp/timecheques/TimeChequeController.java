package eu.nabahilfe.webapp.timecheques;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import eu.nabahilfe.webapp.NbhConst;
import eu.nabahilfe.webapp.domaintypes.AmountDomainType;
import eu.nabahilfe.webapp.domaintypes.AmountDomainValueRepository;
import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.members.MemberRepository;
import eu.nabahilfe.webapp.security.SecurityUtils;
import eu.nabahilfe.webapp.timetransfers.TimeTransfer;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/timecheques")
@SessionAttributes("timeCheque")
public class TimeChequeController {

    private static final Logger log = LoggerFactory.getLogger(TimeChequeController.class);

    private final TimeChequeRepository timeChequeRepository;
    private final MemberRepository memberRepository;
    private final TimeChequeRepository timeCheckRepository;
    private final SecurityUtils securityUtils;
    private final AmountDomainValueRepository amountDomainValueRepository;

    // ...existing code...

    public TimeChequeController(TimeChequeRepository timeChequeRepository, MemberRepository memberRepository,
            TimeChequeRepository timeCheckRepository, SecurityUtils securityUtils,
            AmountDomainValueRepository amountDomainValueRepository) {
        this.timeChequeRepository = timeChequeRepository;
        this.memberRepository = memberRepository;
        this.timeCheckRepository = timeCheckRepository;
        this.securityUtils = securityUtils;
        this.amountDomainValueRepository = amountDomainValueRepository;
    }


    // FIXME: add @Attribute methods to populate Model with common data

    // --------------------
    // LIST & DETAIL
    // --------------------

    // FIXME: Allow only display of own TimeCheques for USER role, all TimeCheques for ADMIN and TIME_KEEPER roles
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    String viewTimeCheque(final Model model, @PathVariable Long id) {

        // Check if user has ADMIN or TIME_KEEPER role
        boolean isAdminOrTimeKeeper = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_TIME_KEEPER"));

        TimeCheque tc = timeChequeRepository.findById(id).orElse(null);
        if (tc == null) {
            model.addAttribute("status", 404);
            model.addAttribute("error", "Not Found");
            model.addAttribute("message", "Zeitscheck mit ID " + id + " nicht gefunden.");
            return "error";
        }

        // If user is not ADMIN or TIME_KEEPER, only allow viewing own TimeCheques
        if (!isAdminOrTimeKeeper && !securityUtils.isAuthenticatedAndMatches(tc.getAssignedTo().getId())) {
            return "redirect:/statuscode/403";
        }

        Member member = memberRepository.findById(tc.getAssignedTo().getId()).orElse(null);
        if (member == null) {
            model.addAttribute("status", 404);
            model.addAttribute("error", "Not Found");
            model.addAttribute("message", "Mitglied mit ID " + tc.getAssignedTo().getId() + " nicht gefunden.");
            return "error";
        }

        model.addAttribute("timeCheque", tc);
        model.addAttribute("member", member);
        model.addAttribute("purchasedTimeCheques", timeCheckRepository.
                findAllByAssignedTo_IdOrderByTransactionDateDesc(member.getId()));

        return "timecheques/summary-timecheque";

    }


    @PreAuthorize("hasAnyRole('ADMIN', 'TIME_KEEPER', 'AUDITOR', 'TREASURER')")
    @GetMapping("/unaccounted")
    String listUnaccountedTimeCheques(final Model model) {
        log.debug("Listing unaccounted TimeCheques");
        model.addAttribute("timeCheques", timeChequeRepository.findAllByAccountedBy_IdIsNullAndAmountGreaterThanOrderByTransactionDateAsc(0.0));
        log.debug("Found {} unaccounted TimeCheques", ((java.util.List<?>) model.getAttribute("timeCheques")).size());
        return "timecheques/list-unaccounted-timecheques";
    }


    // --------------------
    // CREATE NEW
    // --------------------

    // Create TimeCheque for specific Member
    @PreAuthorize("hasAnyRole('ADMIN', 'TIME_KEEPER')")
    @GetMapping("/new")
    String addTimeCheque(final Model model, @RequestParam Long memberId) {

        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            model.addAttribute("errorMessage", "Mitglied mit ID " + memberId + " nicht gefunden.");
            return "error";
        }

        TimeCheque tc = createNewTimeCheque(member);

        model.addAttribute("timeCheque", tc);
        model.addAttribute("purchasedTimeCheques", timeCheckRepository.findAllByAssignedTo_IdOrderByTransactionDateDesc(memberId));
        model.addAttribute("pricePerHour", resolveTimechequeeFeePerHour(LocalDate.now()).floatValue());

        String validationError = validateData(member, tc, timeChequeRepository.countByAssignedTo(member), false);
        if (validationError != null) {
            model.addAttribute("errorMessage", validationError);
            log.debug("Validation error for TimeCheque for Member id={}: {}", memberId, validationError);
        }

        return "timecheques/create-timecheque";
    }


    // Business Logic: determine TimeCheque hours based on existing TimeCheques
    private TimeCheque createNewTimeCheque(Member member) {
        int existingTimeCheques = timeChequeRepository.countByAssignedTo(member);
        if (existingTimeCheques == 0 && (! member.getIsImportedMember())) {
            log.debug("Member id={} has no existing TimeCheques, is not imported Member, using first hours of {}", member.getId(), NbhConst.FIRST_TIME_CHEQUE_HOURS);
            return createTimeCheque(NbhConst.FIRST_TIME_CHEQUE_HOURS, member);
        }
        log.debug("Member id={} has {} existing TimeCheques, using regular hours of {}", member.getId(), existingTimeCheques, NbhConst.REGULAR_TIME_CHEQUE_HOURS);
        return createTimeCheque(NbhConst.REGULAR_TIME_CHEQUE_HOURS, member);
    }


    // Create TimeCheque for self Member
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/newfromself/{memberId}")
    String addSelfTimeCheque(final Model model, @PathVariable Long memberId) {

        // For security, only allow access to buy timecheques where the current user is the logged in user
        if (!securityUtils.isAuthenticatedAndMatches(memberId)) {
            return "redirect:/statuscode/403";
        }

        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            model.addAttribute("errorMessage", "Mitglied mit ID " + memberId + " nicht gefunden.");
            return "error";
        }

        TimeCheque tc = createNewTimeCheque(member);

        model.addAttribute("timeCheque", tc);
        model.addAttribute("purchasedTimeCheques", timeCheckRepository.findAllByAssignedTo_IdOrderByTransactionDateDesc(memberId));
        model.addAttribute("pricePerHour", resolveTimechequeeFeePerHour(LocalDate.now()).floatValue());

        String validationError = validateData(member, tc, timeChequeRepository.countByAssignedTo(member), true);
        if (validationError != null) {
            model.addAttribute("errorMessage", validationError);
            log.debug("Validation error for TimeCheque for Member id={}: {}", memberId, validationError);
        }

        return "timecheques/create-timecheque";
    }



    // Save the new TimeCheque and update Member's accumulated hours

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Transactional
    String saveTimeCheque(final Model model, @RequestParam LocalDate orderDate, @RequestParam int hours) {

        TimeCheque tc = (TimeCheque) model.getAttribute("timeCheque");
        tc.setTransactionDate(orderDate);
        tc.setHours(hours);
        BigDecimal pricePerHour = resolveTimechequeeFeePerHour(orderDate);
        tc.setAmount(hours <= 5 ? BigDecimal.valueOf(0) : pricePerHour.multiply(BigDecimal.valueOf(hours)));
        timeChequeRepository.save(tc);

        // BusinessRule: Update Member's accumulated hours
        if (tc.getAssignedTo().getAccumulatedHours() == null) {
            tc.getAssignedTo().setAccumulatedHours(0);
        }
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

        tc.setHours(timeChequeHours);
        BigDecimal pricePerHour = resolveTimechequeeFeePerHour(LocalDate.now());
        tc.setAmount(timeChequeHours <= 5 ? BigDecimal.valueOf(0) : pricePerHour.multiply(BigDecimal.valueOf(timeChequeHours)));
        tc.setAssignedTo(member);
        tc.setTransactionDate(LocalDate.now());

        log.debug("\nCreated TimeCheque: {}", tc);

        return tc;
    }


    private BigDecimal resolveTimechequeeFeePerHour(LocalDate date) {
        return amountDomainValueRepository
                .findByCodeAndDate(AmountDomainType.TIMECHEQUE_FEE.name(), date)
                .map(adv -> adv.getAmount())
                .orElseThrow(() -> new IllegalStateException(
                        "Kein TIMECHEQUE_FEE Eintrag für das Datum " + date + " gefunden."));
    }

    private String validateData(Member member, TimeCheque timeCheque, int existingTimeCheques, boolean isSelfPurchase) {
        // Business Rule: TimeCheques can only be purchased if Member has less than 5 accumulated hours,
        // except for the first TimeCheque, which is free of charge.
        if (member.getAccumulatedHours() != null && member.getAccumulatedHours() >= NbhConst.MIN_HOURS_FOR_TIME_CHEQUE && existingTimeCheques > 0) {
            return "Zeitschecks können erst bei weniger als 5 Stunden Zeitguthaben erworben werden." +
                   " Aktuelles Zeitguthaben: " + member.getAccumulatedHours() + " Stunden.";
        }
        // Business Rule: Self-purchase of TimeCheques is only allowed if directDebitAuthorization is true.
        if (isSelfPurchase && !member.getDirectDebitAuthorization()) {
            return "Selbstkauf von Zeitschecks ist nur möglich wenn eine Lastschriftgenehmigung vorliegt.";
        }
        return null;
    }



}
