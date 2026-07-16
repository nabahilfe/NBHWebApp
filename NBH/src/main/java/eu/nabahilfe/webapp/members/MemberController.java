/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.members;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
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
import eu.nabahilfe.webapp.accountings.AccountingEntry;
import eu.nabahilfe.webapp.accountings.AccountingRepository;
import eu.nabahilfe.webapp.accountings.TransactionType;
import eu.nabahilfe.webapp.domaintypes.AmountDomainType;
import eu.nabahilfe.webapp.domaintypes.AmountDomainValue;
import eu.nabahilfe.webapp.domaintypes.AmountDomainValueRepository;
import eu.nabahilfe.webapp.osm.Address;
import eu.nabahilfe.webapp.osm.NominatimResult;
import eu.nabahilfe.webapp.osm.NominatimService;
import eu.nabahilfe.webapp.security.SecurityUtils;
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
    private final SecurityUtils securityUtils;
    private final MembershipFeeRepository membershipFeeRepository;
    private final AmountDomainValueRepository amountDomainValueRepository;
    private final AccountingRepository accountingRepository;
    private final NominatimService nominatimService;

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    public MemberController(MemberRepository memberRepository, RoleRepository roleRepository,
            TimeTransferRepository timeTransferRepository, TimeChequeRepository timeCheckRepository,
            SecurityUtils securityUtils, MembershipFeeRepository membershipFeeRepository,
            AmountDomainValueRepository amountDomainValueRepository, AccountingRepository accountingRepository,
            NominatimService nominatimService) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.timeTransferRepository = timeTransferRepository;
        this.timeCheckRepository = timeCheckRepository;
        this.securityUtils = securityUtils;
        this.nominatimService = nominatimService;
        this.amountDomainValueRepository = amountDomainValueRepository;
        this.membershipFeeRepository = membershipFeeRepository;
        this.accountingRepository = accountingRepository;
    }


    @PreAuthorize("hasRole('USER')")
    @ModelAttribute("joiningDateMin")
    public String joiningDateMin() {
        return LocalDate.now().minusMonths(2).withDayOfMonth(1).toString();
    }

    @PreAuthorize("hasRole('USER')")
    @ModelAttribute("resignationDateMax")
    public String resignationDateMax() {
        LocalDate d = LocalDate.now().plusMonths(2);
        return d.withDayOfMonth(d.lengthOfMonth()).toString();
    }

    @PreAuthorize("hasRole('USER')")
    @ModelAttribute("member")
    public Member findMember(@PathVariable(required = false) Long id) {
        return id == null ? new Member() : memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + id
                        + ". Please ensure the ID is correct and the member exists in the database."));
    }

    @PreAuthorize("hasRole('USER')")
    @ModelAttribute("roles")
    public List<Role> getAllRoles() {
        return roleRepository.findAllBy(Sort.by("roleName").ascending())
                .stream()
                .filter(r -> !NbhConst.ADMIN_ROLE_NAME.equals(r.getRoleName()))
                .collect(java.util.stream.Collectors.toList());
    }


    @PreAuthorize("hasRole('USER')")
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

    @PreAuthorize("hasAnyRole('ADMIN', 'TIME_KEEPER', 'AUDITOR', 'TREASURER')")
    @GetMapping("/unaccounted-mbshipfees")
    String listUnaccountedMembershipFees(final Model model) {
        log.debug("Listing unaccounted MembershipFees");
        model.addAttribute("membershipFees", membershipFeeRepository.findByAccountedByIsNullAndDoNotChargeFalse());
        log.debug("Found {} unaccounted MembershipFees", ((java.util.List<?>) model.getAttribute("membershipFees")).size());
        return "members/list-unaccounted-membershipfees";
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'TIME_KEEPER', 'AUDITOR', 'TREASURER')")
    @GetMapping("/open-membership-fees")
    String listOpenMembershipFees(final Model model, @RequestParam(required = false) Year year) {

        // year = (year != null) ? year : Year.now();
        year = Year.now();

        log.debug("Listing open MembershipFees for year {}", year);
        List<MembershipFeeOpenForm> open = membershipFeeRepository.findMembersWithoutFeeForYear(year);
        model.addAttribute("membershipFeesOpen", open);
        log.debug("Found {} open MembershipFees for year {}", open.size(), year);
        return "members/list-open-membership-fees";
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/search")
    public String searchMembers(@RequestParam String searchTerm, Model model, RedirectAttributes redirectAttributes) {
        // save current value of searchTerm
        model.addAttribute("searchTerm", searchTerm);
        log.debug("Searching Members by '{}'", searchTerm);
        return "redirect:/members";
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'BOARD_MEMBER')")
    @GetMapping("/resigned")
    public String listResignedMembers(Model model, RedirectAttributes redirectAttributes,
                                      jakarta.servlet.http.HttpSession session) {
        // remove searchTerm from session so all resigned members are shown unfiltered
        session.removeAttribute("searchTerm");
        model.addAttribute("searchTerm", "");
        redirectAttributes.addFlashAttribute("resignedOnly", Boolean.TRUE);
        return "redirect:/members";
    }



    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'BOARD_MEMBER')")
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

        Boolean resignedOnly = (model.getAttribute("resignedOnly") != null && (Boolean) model.getAttribute("resignedOnly") ? Boolean.TRUE : Boolean.FALSE);

        Page<Member> memberPage = null;
        if (searchTerm.length() <= 0) {
            if (resignedOnly) {
                memberPage = memberRepository.findAllInactive(pageRequest);
            } else {
                memberPage = memberRepository.findAllActive(pageRequest);
            }
        } else {
            Integer memberNmbr = null;
            if (searchTerm.matches("\\d+")) {
                try {
                    memberNmbr = Integer.parseInt(searchTerm);
                }
                catch (NumberFormatException e) {
                    log.warn("Failed to parse searchTerm '{}' as member number, ignoring numeric search term", searchTerm);
                }
            }
            if (resignedOnly) {
                memberPage = memberRepository.findAllInactive(pageRequest);
            } else {
                memberPage = memberRepository.findAllActiveByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMemberNmbr(
                        searchTerm, searchTerm, memberNmbr, pageRequest);
            }
        }

        model.addAttribute("memberPage", memberPage);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("order", order);

        redirectAttributes.addFlashAttribute("orderBy", orderBy);
        redirectAttributes.addFlashAttribute("order", order);

        log.debug("\nFound {} members matching serchTerm='{}' and sort='{}'", memberPage.getTotalElements(), searchTerm, sort);

        int totalPages = memberPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        if (resignedOnly)
            return "members/list-resigned-members";
        else
            return "members/list-members";
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'BOARD_MEMBER')")
    @GetMapping("/{id}")
    String editMember(final Model model, @PathVariable Long id, RedirectAttributes redirectAttributes,
                      @RequestParam(required = false) Integer year,
                      jakarta.servlet.http.HttpSession session) {
        Optional<Member> member = memberRepository.findById(id);

        if (member.isPresent() && isSystemAdmin(member.get())) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Das Mitglied 'System Administrator' kann nicht geändert werden.");
            return "redirect:/members";
        }

        log.debug("Editing Member: {}", id);

        String sessionKey = "selectedYear_" + id;
        int resolvedYear;
        if (year != null) {
            resolvedYear = year;
            session.setAttribute(sessionKey, resolvedYear);
        } else {
            Object stored = session.getAttribute(sessionKey);
            resolvedYear = stored instanceof Integer ? (Integer) stored : LocalDate.now().getYear();
        }

        model.addAttribute("selectedYear", resolvedYear);
        model.addAttribute("receivedTimeTransfers", timeTransferRepository.findAllByToMember_IdAndYearOrderByDateOfServiceDesc(id, resolvedYear));
        model.addAttribute("givenTimeTransfers", timeTransferRepository.findAllByFromMember_IdAndYearOrderByDateOfServiceDesc(id, resolvedYear));
        model.addAttribute("purchasedTimeCheques", timeCheckRepository.findAllByAssignedTo_IdAndYearOrderByTransactionDateDesc(id, resolvedYear));
        return "members/detail-member";
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'BOARD_MEMBER')")
    @GetMapping("/statistics")
    String memberStatistics(final Model model) {
        // Merge joined and resigned counts by year into a combined list
        List<Object[]> joined = memberRepository.findJoinedCountPerYear();
        List<Object[]> resigned = memberRepository.findResignedCountPerYear();

        // Build a sorted map: year -> [joinedCount, resignedCount]
        java.util.TreeMap<Integer, long[]> byYear = new java.util.TreeMap<>();
        for (Object[] row : joined) {
            int year = ((Number) row[0]).intValue();
            byYear.computeIfAbsent(year, _ -> extracted())[0] = ((Number) row[1]).longValue();
        }
        for (Object[] row : resigned) {
            int year = ((Number) row[0]).intValue();
            byYear.computeIfAbsent(year, _ -> extracted())[1] = ((Number) row[1]).longValue();
        }

        model.addAttribute("memberStats", byYear);
        return "members/member-statistics";
    }


    private long[] extracted() {
        return new long[2];
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BOARD_MEMBER')")
    @GetMapping("/birthdays")
    String listBirthdays(final Model model) {
        model.addAttribute("currentMonth", memberRepository.findBirthdaysByMonthOffset(0));
        model.addAttribute("nexttMonth", memberRepository.findBirthdaysByMonthOffset(1));
        return "members/birthdays";
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mydata/{id}")
    String myData(final Model model, @PathVariable Long id,
                  @RequestParam(required = false) Integer year,
                  jakarta.servlet.http.HttpSession session) {
        log.debug("Showing /users/mydata/{}", id);
        // Ensure that the member has only access to his own data
        // get current authenticated user from security context
        Member current = securityUtils.getCurrentUser();
        if (current == null) {
            model.addAttribute("errorMessage", "Nicht authentifiziert.");
            return "redirect:/login";
        }

        // verify requested id matches current session user by id and email
        // prefer centralized helper: authenticated + id match
        if (!securityUtils.isAuthenticatedAndMatches(id)) {
            // either not authenticated (handled above) or id mismatch
            log.warn("Unauthorized access attempt to /members/mydata/{} by user {}", id, current.getEmail());
            return "redirect:/statuscode/403";
        }

        // now safe to load the member record
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + id));

        // remember selected year in session; fall back to session value, then current year
        String sessionKey = "selectedYear_" + id;
        int resolvedYear;
        if (year != null) {
            resolvedYear = year;
            session.setAttribute(sessionKey, resolvedYear);
        } else {
            Object stored = session.getAttribute(sessionKey);
            resolvedYear = stored instanceof Integer ? (Integer) stored : LocalDate.now().getYear();
        }

        model.addAttribute("selectedYear", resolvedYear);
        model.addAttribute("roleNames", member.getRole() != null ? member.getRole().getRoleName() : "Mitglied");
        model.addAttribute("receivedTimeTransfers", timeTransferRepository.findAllByToMember_IdAndYearOrderByDateOfServiceDesc(id, resolvedYear));
        model.addAttribute("givenTimeTransfers", timeTransferRepository.findAllByFromMember_IdAndYearOrderByDateOfServiceDesc(id, resolvedYear));
        model.addAttribute("purchasedTimeCheques", timeCheckRepository.findAllByAssignedTo_IdAndYearOrderByTransactionDateDesc(id, resolvedYear));
        return "members/view-member-data";
     }

    // --------------------
    // CREATE NEW, UPDATE
    // --------------------



    @PreAuthorize("hasAnyRole('ADMIN', 'BOARD_MEMBER')")
    @GetMapping("/new")
    String newMember(final Model model) {
        log.debug("Creating new Member");
        return "members/detail-member";
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'BOARD_MEMBER')")
    @Transactional(rollbackOn = Exception.class)
    @PostMapping
    public String saveMember(Model model, @ModelAttribute @Valid Member member,
                BindingResult result, RedirectAttributes redirectAttributes) {

        log.debug("Will Save Member afer Validation: {}", member);

        // Protect System-Administrator member from being modified
        if (member.getId() != null && isSystemAdminById(member.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Das Mitglied 'System Administrator' kann nicht geändert werden.");
            return "redirect:/members";
        }
        if (member.getEmail() != null && member.getEmail().toLowerCase().startsWith(NbhConst.ADMIN_EMAIL_PREFIX)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Die E-Mail-Adresse '" + NbhConst.ADMIN_EMAIL_PREFIX + "' ist für den System-Administrator reserviert.");
            return "redirect:/members";
        }

        if (member.getId() != null && isSozialkonto(member)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Das Sozialkonto kann nicht geändert werden.");
            return "redirect:/members";
        }

        String validationError = validateData(member);
        if (validationError != null) {
            model.addAttribute("errorMessage", validationError);
            return "members/detail-member";
        }

        String error = validateOnlyOneSozialkonto(member);
        if (error != null) {
            model.addAttribute("errorMessage", error);
            return "members/detail-member";
        }

        error = validateOnlyOneSysAdmin(member);
        if (error != null) {
            model.addAttribute("errorMessage", error);
            return "members/detail-member";
        }

        // ensure that no member can be created as system account
        member.setIsSystemAccount(false);

        if (member.getId() == null) {
            member.setMemberNmbr(getNextMemberNumber());
            if (member.getJoiningDate() == null) {
                member.setJoiningDate(LocalDate.now().withDayOfMonth(1));
            }
            member.setIsImportedMember(false);
        }


        log.debug("Saving Member: {}", member);

        validateMemberAddress(member);

        memberRepository.save(member);

        redirectAttributes.addFlashAttribute("numberOfTimecheques", timeCheckRepository.countByAssignedTo(member));
        redirectAttributes.addFlashAttribute("successMessage", "Daten für " + member.getName() + " wurden gespeichert.");

        log.debug("Member saved: {}", member);

        return "redirect:/members/" + member.getId();
    }


    // we use OpenStreetMap Nominatim API to validate and geocode the member address
    private void validateMemberAddress(@Valid Member m) {

        // TODO: Country should be a field in the Member entity, not hardcoded to "Österreich"
        Address address = new Address(m.getStreet(), m.getNumber(), m.getZip(), m.getCity(), "Österreich");
        Optional<NominatimResult> result = nominatimService.localizeAddress(address);

        if (result.isPresent()) {
            NominatimResult loc = result.get();

            // we need to check if we have the real address - displaName must start with number and street, zip and city must be contained
            // otherwise we have a wrong address (e.g. if the number is not found, it will return the city center)

            String displayNameUPPER = loc.displayName().toUpperCase();
            if (displayNameUPPER.startsWith(m.getNumber()) && displayNameUPPER.contains(m.getStreet().toUpperCase()) &&
                displayNameUPPER.contains(m.getZip()) && displayNameUPPER.contains(m.getCity().toUpperCase())) {

                log.error("Address validated and geocoded: {} -> lat: {}, lon: {}", address, loc.getLatitude(), loc.getLongitude());
                m.setLatitude(loc.getLatitude());
                m.setLongitude(loc.getLongitude());
            }
            else {
                log.error("Address could not be validated: {} -> geocoded to {}, which does not match the input address", address, loc.displayName());
                m.setLatitude(null);
                m.setLongitude(null);
                return;
            }
        }
        else {
            log.error("Address could not be validated or geocoded: {}", address);
            m.setLatitude(null);
            m.setLongitude(null);
        }
    }


    private String validateOnlyOneSozialkonto(@Valid Member member) {
        if (member.isSozialkonto() && memberRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(NbhConst.SOZIALKONTO_FIRST_NAME, NbhConst.SOZIALKONTO_LAST_NAME).size() > 0) {
            return "Es gibt bereits ein Sozialkonto, es kann kein weiteres Sozialkonto angelegt werden!";
        }

        return null;
    }


    private String validateOnlyOneSysAdmin(@Valid Member member) {
        if (member.isSystemAdmin() && memberRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(NbhConst.ADMIN_ACCOUNT_FIRST_NAME, NbhConst.ADMIN_ACCOUNT_LAST_NAME).size() > 0) {
            return "Es gibt bereits einen System-Administrator, es kann kein weiterer System-Administrator angelegt werden!";
        }

        return null;
    }


    private Integer getNextMemberNumber() {
        Integer nmbr =  memberRepository.findTopByOrderByMemberNmbrDesc()
                .map(m -> m.getMemberNmbr() + 1)
                .orElse(NbhConst.START_MEMBER_NUMBER);

        if (nmbr < NbhConst.START_MEMBER_NUMBER) {
            nmbr = NbhConst.START_MEMBER_NUMBER;
        }

        return nmbr;
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'BOARD_MEMBER')")
    @PostMapping("/{id}")
    public String updateMember(Model model, @ModelAttribute @Valid Member member,
            BindingResult result, @RequestParam(required = false) Long roleId,
            RedirectAttributes redirectAttributes, @PathVariable Long id) {
        log.debug("Update Member with id {}: {}", id, member);
        return saveMember(model, member, result, redirectAttributes);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'TIME_KEEPER', 'AUDITOR', 'TREASURER')")
    @Transactional(rollbackOn = Exception.class)
    @PostMapping("/create-fees-batch")
    String createFeesBatch(@RequestParam(required = false) List<Long> memberIds,
            @RequestParam(required = false) List<Boolean> doNotChargeFlags,
            RedirectAttributes redirectAttributes) {

        int count = memberIds == null ? 0 : memberIds.size();
        log.info("createFeesBatch called with {} members", count);

        if (memberIds != null) {

            Optional<AmountDomainValue> value = amountDomainValueRepository.findByCodeAndDate(AmountDomainType.MEMBERSHIP_FEE.name(), LocalDate.now());
            if (value.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Es konnte kein gültiger Mitgliedsbeitrag gefunden werden. Die Konfiguration der Mitgliedsbeiträge prüfen!");
                return "redirect:/members/open-membership-fees";
            }


            for (int i = 0; i < memberIds.size(); i++) {

                Long memberId = memberIds.get(i);
                boolean doNotCharge = Boolean.TRUE.equals(doNotChargeFlags.get(i));
                log.info("-> memberId: {}, doNotCharge: {}", memberId, doNotCharge);

                MembershipFee fee = new MembershipFee();
                fee.setMember(memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId)));
                fee.setForYear(Year.now());
                fee.setDoNotCharge(doNotCharge);
                fee.setTransactionDate(LocalDate.now());

                if (doNotCharge) {
                    fee.setAmount(BigDecimal.ZERO);
                    membershipFeeRepository.save(fee);

                    // create dummy accounting entry to mark this fee as accounted without actual financial transaction
                    AccountingEntry entry = new AccountingEntry();
                    entry.setAccountingDate(LocalDate.now());
                    entry.setAccountableName(fee.getAccountableName());
                    entry.setTransactionDate(LocalDate.now());
                    entry.setTransactionAmount(BigDecimal.ZERO);
                    entry.setTransactionType(TransactionType.INCOME.name());
                    entry.setAccountableName(entry.getAccountableName());
                    entry.setAccountableMember(fee.getMember());
                    entry.setDescription("Keinen Beitrag einheben für " + fee.getForYear());
                    accountingRepository.save(entry);
                }
                else {
                    fee.setAmount(value.get().getAmount());
                    membershipFeeRepository.save(fee);
                }
            }
        }

        log.info("Created {} MembershipFee records", count);

        redirectAttributes.addFlashAttribute("successMessage",
                "Beitragsvorschreibungen für " + count + " Mitglied(er) wurden erstellt.");
        return "redirect:/members/open-membership-fees";
    }


    // --------------------
    // LÖSCHEN
    // --------------------

    @PreAuthorize("hasAnyRole('ADMIN', 'BOARD_MEMBER')")
    @Transactional(rollbackOn = Exception.class)
    @PostMapping("/delete/{id}")
    String deletMember(Model model, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Member> member = memberRepository.findById(id);
        if (member.isPresent() && isSystemAdmin(member.get())) {
            redirectAttributes.addFlashAttribute("resignedOnly", Boolean.TRUE);
            redirectAttributes.addFlashAttribute("errorMessage", "Das Mitglied 'System Administrator' kann nicht gelöscht werden.");
            return "redirect:/members";
        }
        if (member.isPresent() && isSozialkonto(member.get())) {
            redirectAttributes.addFlashAttribute("resignedOnly", Boolean.TRUE);
            redirectAttributes.addFlashAttribute("errorMessage", "Das Sozialkonto kann nicht gelöscht werden.");
            return "redirect:/members";
        }

        Member m = member.orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + id));
        Integer tcHours = m.getAccumulatedHours() != null ? m.getAccumulatedHours() : 0;

        String error = transferTimeChequesToSozialkonto(m);
        if (error != null) {
            redirectAttributes.addFlashAttribute("resignedOnly", Boolean.TRUE);
            redirectAttributes.addFlashAttribute("errorMessage", error);
            return "redirect:/members";
        }

        anonymizeMemberData(member.get());

        redirectAttributes.addFlashAttribute("resignedOnly", Boolean.TRUE);
        redirectAttributes.addFlashAttribute("successMessage", "Mitgliedsdaten wurden anonymisiert und gelöscht, "
                + tcHours + " vorhandene Stunde(n) wurden zum Sozialkonto übertragen.");

        return "redirect:/members";
    }


    private void anonymizeMemberData(Member member) {
        member.setSalutation(null);
        member.setTitle(null);
        member.setInstitution(null);
        member.setBirthdate(LocalDate.parse("1900-01-01"));
        member.setFirstName("*");
        member.setLastName("*");
        member.setEmail(null);
        member.setPhoneNumber(null);
        member.setRole(null);
        member.setStreet("*");
        member.setNumber("*");
        member.setZip("*");
        member.setCity("*");
        member.setAccumulatedHours(null);
        member.setDirectDebitAuthorization(false);
        // do not overwrite memberNmbr, joiningDate and resignationDate to preserve historical data and referential integrity
        memberRepository.save(member);
        return;
    }


    private String transferTimeChequesToSozialkonto(Member member) {
        if (member.getAccumulatedHours() == null || member.getAccumulatedHours().intValue() <= 0) {
            return null; // No time cheques to transfer
        }

        Member sozialkonto = memberRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(NbhConst.SOZIALKONTO_FIRST_NAME, NbhConst.SOZIALKONTO_LAST_NAME).stream().findFirst().orElse(null);
        if (sozialkonto == null) {
            log.error("Sozialkonto not found. Cannot transfer time cheques.");
            return "Kein Sozialkonto gefunden. Zeitgutscheine konnten nicht übertragen werden, löschen abgebrochen.";
        }

        if (sozialkonto.getAccumulatedHours() == null) {
            sozialkonto.setAccumulatedHours(0);
        }
        sozialkonto.setAccumulatedHours(sozialkonto.getAccumulatedHours() + member.getAccumulatedHours());
        member.setAccumulatedHours(0);

        memberRepository.save(sozialkonto);
        memberRepository.save(member);

        return null;
    }


    // ------------------
    // validating member data
    // ------------------

    private String validateData(Member member) {
        LocalDate currentDate = LocalDate.now();

        // check age
        if (Period.between(member.getBirthdate(), currentDate).getYears() < NbhConst.MIN_MEMBER_AGE)
            return "Die Person ist noch nicht " + NbhConst.MIN_MEMBER_AGE + " Jahre alt!";

        // joiningDate rules (only for new members — existing members have a locked joiningDate)
        if (member.getId() == null && member.getJoiningDate() != null) {
            LocalDate joiningDate = member.getJoiningDate();
            // must not be more than 2 months in the past (starting from the 1st of that month)
            LocalDate earliestJoining = currentDate.minusMonths(2).withDayOfMonth(1);
            if (joiningDate.isBefore(earliestJoining))
                return "Das Beitrittsdatum darf nicht mehr als 2 Monate in der Vergangenheit liegen (frühestens: " + earliestJoining + ").";
        }

        // resignationDate rules
        if (member.getResignationDate() != null) {
            LocalDate resignationDate = member.getResignationDate();
            // must be after joiningDate
            if (member.getJoiningDate() != null && !resignationDate.isAfter(member.getJoiningDate()))
                return "Das Austrittsdatum muss nach dem Beitrittsdatum liegen.";
            // must not be more than 2 months in the future
            LocalDate latestResignation = currentDate.plusMonths(2);
            latestResignation = latestResignation.withDayOfMonth(latestResignation.lengthOfMonth());
            if (resignationDate.isAfter(latestResignation))
                return "Das Austrittsdatum darf nicht mehr als 2 Monate in der Zukunft liegen (spätestens: " + latestResignation + ").";
        }

        // phoneNumber rules
        if (member.getPhoneNumber() != null && !member.getPhoneNumber().isBlank()) {
            String phone = member.getPhoneNumber();
            if (!phone.matches("(\\+[1-9][0-9 ]*|0[0-9 ]*)"))
                return "Telefonnummer muss mit + (dann keine 0 vor der Vorwahl) oder 0 beginnen und darf nur Ziffern und Leerzeichen enthalten.";
        }

        // do not allow sysadmin in email
        if (member.getEmail() != null && member.getEmail().toLowerCase().startsWith(NbhConst.ADMIN_EMAIL_PREFIX))
            return "Die E-Mail-Adresse '" + NbhConst.ADMIN_EMAIL_PREFIX + "' ist für den System-Administrator reserviert und kann nicht verwendet werden!";

        return null;
    }


    // ------------------
    // System-Konto protection
    // ------------------

    private boolean isSystemAdmin(Member member) {
        if (member.getEmail() == null) return false;
        return member.getEmail().toLowerCase().startsWith(NbhConst.ADMIN_EMAIL_PREFIX);
    }

    private boolean isSystemAdminById(Long id) {
        return memberRepository.findById(id)
                .map(this::isSystemAdmin)
                .orElse(false);
    }

    private boolean isSozialkonto(Member member) {
        return member.isSozialkonto();
    }



}
