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


@Controller
@RequestMapping("/accountings")
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

        accountingEntry.setId(99999L);  // dummy ID for the moment

        log.debug("AccountingEntry prepared for booking: " + accountingEntry.toString());

        model.addAttribute("accountingEntry", accountingEntry);
        model.addAttribute("memberName", true);

        return "accountings/detail-accountable";
    }



}
