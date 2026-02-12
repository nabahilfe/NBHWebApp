package eu.nabahilfe.webapp.members;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import eu.nabahilfe.webapp.NbhConst;
import eu.nabahilfe.webapp.timecheques.TimeChequeRepository;
import eu.nabahilfe.webapp.timetransfers.TimeTransferRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/members")
@SessionAttributes("searchTerm")
public class MemberController {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final TimeTransferRepository timeTransferRepository;
    private final TimeChequeRepository timeCheckRepository;


    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    public MemberController(MemberRepository memberRepository, RoleRepository roleRepository,
            TimeTransferRepository timeTransferRepository, TimeChequeRepository timeCheckRepository) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.timeTransferRepository = timeTransferRepository;
        this.timeCheckRepository = timeCheckRepository;
    }


    @ModelAttribute("member")
    public Member findMember(@PathVariable(required = false) Long id) {
        return id == null ? new Member() : memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + id
                        + ". Please ensure the ID is correct and the member exists in the database."));
    }

    @ModelAttribute("roles")
    public List<Role> getAllRoles() {
        return roleRepository.findAllBy(Sort.by("roleName").ascending());
    }


    @ModelAttribute("numberOfTimecheques")
    public Integer getNumberOfTimecheques(@ModelAttribute Member member) {
        if (member.getId() != null)
            return timeCheckRepository.countByAssignedTo(member);
        else
            return 0;
    }


    // ----------------------
    // SEARCH, LIST & DETAIL
    // ----------------------

    @GetMapping("/search")
    public String searchMembers(@RequestParam String searchTerm, Model model, RedirectAttributes redirectAttributes) {
        // save current value of searchTerm
        model.addAttribute("searchTerm", searchTerm);
        log.debug("Searching Members by '{}'", searchTerm);
        return "redirect:/members";
    }


    @GetMapping
    String listAllMembersPaginated(final Model model,
            @RequestParam(required = false, defaultValue = "lastName") String orderBy,
                @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size,
            RedirectAttributes redirectAttributes) {

        String searchTerm = (model.getAttribute("searchTerm") != null ?
                model.getAttribute("searchTerm").toString().trim() : "");

        int currentPage = page.orElse(0);
        int pageSize = size.orElse(NbhConst.PAGINATION_PAGE_SIZE);

        log.debug("\nListing Members - page: {}, size: {}, orderBy: {}, order: {}, filter: '{}'",
                currentPage, pageSize, orderBy, order, searchTerm);

        Sort.Direction dir = order.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(dir, orderBy);

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);

        Page<Member> memberPage = null;
        if (searchTerm.length() <= 0)
            memberPage = memberRepository.findAll(pageRequest);
        else
            memberPage = memberRepository.findAllByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(
                    searchTerm, searchTerm, pageRequest);

        model.addAttribute("memberPage", memberPage);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("order", order);

        redirectAttributes.addFlashAttribute("orderBy", orderBy);
        redirectAttributes.addFlashAttribute("order", order);

        log.debug("\nFound {} members matching filter '{}'", memberPage.getTotalElements(), searchTerm);

        int totalPages = memberPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "members/list-members";
    }


    @GetMapping("/{id}")
    String editMember(final Model model, @PathVariable Long id) {
        log.debug("Editing Member: {}", id);
        model.addAttribute("receivedTimeTransfers", timeTransferRepository.findAllByToMember_IdOrderByDateOfServiceDesc(id));
        model.addAttribute("givenTimeTransfers", timeTransferRepository.findAllByFromMember_IdOrderByDateOfServiceDesc(id));
        model.addAttribute("purchasedTimeCheques", timeCheckRepository.findAllByAssignedTo_IdOrderByTransactionDateDesc(id));
        return "members/detail-member";
    }


    @GetMapping("/register")
    String registerMember(final Model model) {
        log.debug("Registering new Member");
        return "members/validate-email";
    }

    @PostMapping("/register")


    @GetMapping("/login")
    String loginMember(final Model model) {
        log.debug("Member login");
        return "members/login-member";
    }


    // --------------------
    // CREATE NEW, UPDATE
    // --------------------

    @GetMapping("/new")
    String newMember(final Model model) {
        log.debug("Creating new Member");
        return "members/detail-member";
    }


    @Transactional
    @PostMapping
    public String saveMember(Model model, @ModelAttribute @Valid Member member,
                RedirectAttributes redirectAttributes, BindingResult result) {

        log.debug("Will Save Member afer Validation: {}", member);

        String validationError = validateData(member);
        if (validationError != null) {
            model.addAttribute("errorMessage", validationError);
            return "members/detail-member";
        }

        if (member.getId() == null) {
            member.setMemberNmbr(getNextMemberNumber());
            member.setJoiningDate(LocalDate.now());
        }

        log.debug("Saving Member: {}", member);

        memberRepository.save(member);

        redirectAttributes.addFlashAttribute("numberOfTimecheques", timeCheckRepository.countByAssignedTo(member));
        redirectAttributes.addFlashAttribute("successMessage", "Daten für " + member.getName() + " wurden gespeichert.");

        log.debug("Member saved: {}", member);

        return "redirect:/members/" + member.getId();
    }


    private Integer getNextMemberNumber() {
        return memberRepository.findTopByOrderByMemberNmbrDesc()
                .map(m -> m.getMemberNmbr() + 1)
                .orElse(NbhConst.START_MEMBER_NUMBER);
    }


    @PostMapping("/{id}")
    public String updateMember(Model model, @ModelAttribute @Valid Member member,
            @RequestParam(required = false) Long roleId,
            RedirectAttributes redirectAttributes, BindingResult result, @PathVariable Long id) {
        log.debug("Update Member with id {}: {}", id, member);
        return saveMember(model, member, redirectAttributes, result);
    }


    // --------------------
    // LÖSCHEN
    // --------------------

    @PostMapping("/delete/{id}")
    @Transactional
    String deletMember(Model model, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Member> member = memberRepository.findById(id);
        memberRepository.delete(member.get());
        redirectAttributes.addFlashAttribute("successMessage", "Mitglied '" + member.get().getName() + "' wurde gelöscht.");
        log.debug("Deleted Member: {}", member.get());
        return "redirect:/members";
    }


    // ------------------
    // validating member data
    // ------------------

    private String validateData(Member member) {
        // check age
        LocalDate currentDate = LocalDate.now();
        if (Period.between(member.getBirthdate(), currentDate).getYears() < NbhConst.MIN_MEMBER_AGE)
            return "Die Person ist noch nicht " + NbhConst.MIN_MEMBER_AGE + " Jahre alt!";
        return null;
    }


}
