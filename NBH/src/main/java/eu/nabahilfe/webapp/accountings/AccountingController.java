package eu.nabahilfe.webapp.accountings;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.members.MemberRepository;
import eu.nabahilfe.webapp.timecheques.TimeCheque;
import eu.nabahilfe.webapp.timecheques.TimeChequeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/accountings")
@SessionAttributes("accountingEntry")
public class AccountingController {

    private final AccountingRepository accountingRepository;
    private final MemberRepository memberRepository;
    private final TimeChequeRepository timeChequeRepository;

    private static final Logger log = LoggerFactory.getLogger(AccountingController.class);

    public AccountingController(AccountingRepository accountingRepository, MemberRepository memberRepository,
            TimeChequeRepository timeChequeRepository) {
        this.accountingRepository = accountingRepository;
        this.memberRepository = memberRepository;
        this.timeChequeRepository = timeChequeRepository;
    }


    @ModelAttribute("formRowData")
    public AccountableRowSelectionForm formRowData() {
        return new AccountableRowSelectionForm();
    }


    // --------------------
    // VIEW
    // --------------------

    @GetMapping("/view-accounting/{id}")
    public String viewAccountingEntry(final Model model, @PathVariable Long id) {

        AccountingEntry accountingEntry = accountingRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Accounting entry not found with id: " + id));

        log.debug("Viewing AccountingEntry: " + accountingEntry.toString());

        model.addAttribute("accountingEntry", accountingEntry);
        return "accountings/view-accounting";
    }


    // --------------------
    // CREATE NEW, UPDATE
    // --------------------


    @PostMapping("/new-accountable")
    public String newAccountable(final Model model, @ModelAttribute @Valid AccountableRowSelectionForm formRowData) {

        log.debug("Preparing AccountingEntry from form data: " + formRowData.toString());

        AccountingEntry accountingEntry = new AccountingEntry();

        accountingEntry.setAccountableClass(formRowData.getAccountableClassName());
        accountingEntry.setAccountableId(formRowData.getAccountableId());
        accountingEntry.setAccountableMember(formRowData.getAccountableMemberId() != null ?
                memberRepository.findById(formRowData.getAccountableMemberId()).orElse(null) : null);
        accountingEntry.setTransactionType(formRowData.getTransactionType());
        accountingEntry.setTransactionDate(LocalDate.parse(formRowData.getTransactionISODate()));
        accountingEntry.setTransactionAmount(formRowData.getTransactionAmount());


        log.debug("AccountingEntry prepared for booking: " + accountingEntry.toString());

        model.addAttribute("accountingEntry", accountingEntry);

        return "accountings/detail-accountable";
    }


    @PostMapping("/save-accounting")
    @Transactional
    public String saveAccountingEntry(final Model model,  @ModelAttribute @Valid AccountingEntry accountingEntry,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        log.debug("Saving AccountingEntry: " + accountingEntry.toString());

        if (bindingResult.hasErrors()) {
            log.debug("Validation errors found: " + bindingResult.getAllErrors().toString());
            model.addAttribute("accountingEntry", accountingEntry);
            redirectAttributes.addFlashAttribute("errorMessage", bindingResult.getAllErrors().toString());
            return "accountings/detail-accountable";
        }

        accountingRepository.save(accountingEntry);

        if (accountingEntry.getAccountableClass().equals(TimeCheque.class.getSimpleName())) {
            TimeCheque tc = timeChequeRepository.findById(accountingEntry.getAccountableId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid TimeCheque ID: " + accountingEntry.getAccountableId()));
            tc.setAccountedBy(accountingEntry);
        }
        else if (accountingEntry.getAccountableClass().equals("SOME_OTHER_CLASS")) {
            // FIXME: Handle other accountable classes as needed
        }


        log.debug("AccountingEntry saved with ID: " + accountingEntry.getId());

        redirectAttributes.addFlashAttribute("accountingEntry", accountingEntry);
        redirectAttributes.addFlashAttribute("successMessage", "Accounting entry saved successfully.");
        return "redirect:/accountings/view-accounting/" + accountingEntry.getId();
    }



}
