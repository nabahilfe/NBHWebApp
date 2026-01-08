package eu.nabahilfe.webapp.accountings;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import eu.nabahilfe.webapp.timecheques.TimeCheckRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/accountings")
public class AccountingController {

    private final TimeCheckRepository timeCheckRepository;
    private final AccountingRepository accountingRepository;

    public AccountingController(TimeCheckRepository timeCheckRepository, AccountingRepository accountingRepository) {
        this.timeCheckRepository = timeCheckRepository;
        this.accountingRepository = accountingRepository;
    }



    @ModelAttribute("accountingEntry")
    public AccountingEntry findAccountingEntry(@PathVariable(required = false) Long id) {
        return id == null ? new AccountingEntry() : accountingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Accounting entry not found with id: " + id));
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
}
