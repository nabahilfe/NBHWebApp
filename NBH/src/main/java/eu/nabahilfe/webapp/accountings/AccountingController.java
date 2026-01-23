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
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


/*
 *  Interface:
 *
 *  public abstract String getAccountableClass();    // MemberFee, TimeCheque, Transaction, ...
    public abstract Long getAccountableId();
    public abstract String getTransactionType();     // INCOME oder EXPENSE - muss aus Enum TransactionType kommen
    public abstract LocalDate getTransactionDate();
    public abstract BigDecimal getTransactionAmount();

    http://localhost:8080/accountings/prepare-accounting/?accClass=TimeCheque&accId=8&trnsType=INCOME&trnsDate=2026-01-19&trnsAmount=36.00
 */



@Controller
@RequestMapping("/accountings")
@SessionAttributes("accountingEntry")
public class AccountingController {

    private final AccountingRepository accountingRepository;
    private final MemberRepository memberRepository;

    private static final Logger log = LoggerFactory.getLogger(AccountingController.class);

    public AccountingController(AccountingRepository accountingRepository, MemberRepository memberRepository) {
        this.accountingRepository = accountingRepository;
        this.memberRepository = memberRepository;
    }


    @ModelAttribute("formRowData")
    public AccountableRowSelectionForm formRowData() {
        return new AccountableRowSelectionForm();
    }

    @GetMapping("/new")
    public String newAccounting(final Model model) {
        return "accountings/create-accounting";
    }

//
//    @Transactional
//    @PostMapping
//    public String createAccounting(final Model model, @ModelAttribute AccountingEntry accountingEntry,
//            RedirectAttributes redirectAttributes, BindingResult result) {
//        accountingRepository.save(accountingEntry);
//        return "redirect:/accountings/" + accountingEntry.getId();
//    }
//
//
//    @Transactional
//    @PostMapping("/{id}")
//    public String updateAccounting(final Model model, @ModelAttribute AccountingEntry accountingEntry,
//            RedirectAttributes redirectAttributes, BindingResult result) {
//        log.debug("Updating AccountingEntry: " + accountingEntry.toString());
//        accountingRepository.save(accountingEntry);
//        return "redirect:/timecheques/unaccounted";
//    }
//
//
//    @GetMapping("/{id}")
//    public String showAccounting(final Model model, @PathVariable Long id) {
//        AccountingEntry accountingEntry = accountingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Accounting entry not found with id: " + id));
//        model.addAttribute("accountingEntry", accountingEntry);
//        return "accountings/summary-accounting";
//
//    }


    @PostMapping("/prepare-accounting")
    public String prepareAccountable(final Model model, @ModelAttribute @Valid AccountableRowSelectionForm formRowData) {
        log.debug("Preparing AccountingEntry from form data: " + formRowData.toString());

        AccountingEntry accountingEntry = new AccountingEntry();

        accountingEntry.setAccountableClass(formRowData.getAccountableClassName());
        accountingEntry.setAccountableId(formRowData.getAccountableId());
        accountingEntry.setAccountableMember(formRowData.getAccountableMemberId() != null ?
                memberRepository.findById(formRowData.getAccountableMemberId()).orElse(null) : null);
        accountingEntry.setTransactionType(formRowData.getTransactionType());
        accountingEntry.setTransactionDate(LocalDate.parse(formRowData.getTransactionISODate()));
        accountingEntry.setTransactionAmount(formRowData.getTransactionAmount());

        accountingEntry.setId(99999L);  // dummy ID for the moment

        log.debug("AccountingEntry prepared for booking: " + accountingEntry.toString());

        model.addAttribute("accountingEntry", accountingEntry);
        model.addAttribute("memberName", true);

        return "show-accounting";
    }



}
