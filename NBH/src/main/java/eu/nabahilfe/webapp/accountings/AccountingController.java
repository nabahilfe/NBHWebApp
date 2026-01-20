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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import eu.nabahilfe.webapp.timecheques.TimeChequeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


/*
 *  Interface:
 *
 *  public abstract String getAccountableClass();    // MemberFee, TimeCheque, Transaction, ...
    public abstract Long getAccountableId();
    public abstract String getTransactionType();    // INCOME oder EXPENSE - muss aus Enum TransactionType kommen
    public abstract LocalDate getTransactionDate();
    public abstract BigDecimal getTransactionAmount();

    http://localhost:8080/accountings/book-accountable/?accClass=TimeCheque&accId=8&trnsType=INCOME&trnsDate=2026-01-19&trnsAmount=36.00
 */



@Controller
@RequestMapping("/accountings")
public class AccountingController {

    private final TimeChequeRepository timeCheckRepository;
    private final AccountingRepository accountingRepository;

    private static final Logger log = LoggerFactory.getLogger(AccountingController.class);

    public AccountingController(TimeChequeRepository timeCheckRepository, AccountingRepository accountingRepository) {
        this.timeCheckRepository = timeCheckRepository;
        this.accountingRepository = accountingRepository;

        LocalDate ld = LocalDate.now();
        ld.toString();
    }


    @GetMapping("/new")
    public String newAccounting(final Model model) {
        return "accountings/create-accounting";
    }


    @Transactional
    @PostMapping
    public String createAccounting(final Model model, @ModelAttribute @Valid AccountingEntry accountingEntry,
            RedirectAttributes redirectAttributes, BindingResult result) {
        accountingRepository.save(accountingEntry);
        return "redirect:/accountings/" + accountingEntry.getId();
    }


    @Transactional
    @PostMapping("/{id}")
    public String updateAccounting(final Model model, @ModelAttribute @Valid AccountingEntry accountingEntry,
            RedirectAttributes redirectAttributes, BindingResult result) {
        accountingRepository.save(accountingEntry);
        return "redirect:/accountings/" + accountingEntry.getId();
    }


    @GetMapping("/{id}")
    public String showAccounting(final Model model, @PathVariable Long id) {
        AccountingEntry accountingEntry = accountingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Accounting entry not found with id: " + id));
        model.addAttribute("accountingEntry", accountingEntry);
        return "accountings/summary-accounting";

    }

    // /book-accountable/?accClass=TimeCheque&accId=8&trnsType=INCOME&trnsDate=2026-01-19&trnsAmount=36.00
    @GetMapping("/book-accountable")
    public String bookAccountable(final Model model,
            @RequestParam String accClass, @RequestParam Long accId,
            @RequestParam String trnsType, @RequestParam String trnsDate, @RequestParam BigDecimal trnsAmount) {

        AccountingEntry accountingEntry = new AccountingEntry();

        accountingEntry.setAccountableClass(accClass);
        accountingEntry.setAccountableId(accId);
        accountingEntry.setTransactionType(trnsType);
        accountingEntry.setTransactionDate(LocalDate.parse(trnsDate));
        accountingEntry.setTransactionAmount(trnsAmount);

        log.debug("AccountingEntry prepared for booking: " + accountingEntry.toString());

        model.addAttribute("accountingEntry", accountingEntry);

        return "";

    }
}
