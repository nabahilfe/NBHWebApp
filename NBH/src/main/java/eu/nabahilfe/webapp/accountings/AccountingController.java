/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.accountings;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import eu.nabahilfe.webapp.members.MemberRepository;
import eu.nabahilfe.webapp.members.MembershipFee;
import eu.nabahilfe.webapp.members.MembershipFeeRepository;
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
    private final MembershipFeeRepository membershipFeeRepository;
    private final TransactionRepository transactionRepository;

    private static final Logger log = LoggerFactory.getLogger(AccountingController.class);

    public AccountingController(AccountingRepository accountingRepository, MemberRepository memberRepository,
            TimeChequeRepository timeChequeRepository, MembershipFeeRepository membershipFeeRepository,
            TransactionRepository transactionRepository) {
        this.accountingRepository = accountingRepository;
        this.memberRepository = memberRepository;
        this.timeChequeRepository = timeChequeRepository;
        this.membershipFeeRepository = membershipFeeRepository;
        this.transactionRepository = transactionRepository;
    }


    @ModelAttribute("formRowData")
    public AccountableRowSelectionForm formRowData() {
        return new AccountableRowSelectionForm();
    }


    // --------------------
    // INCOME FORM
    // --------------------

    @PreAuthorize("hasAnyRole('ADMIN', 'BOARD_MEMBER', 'TREASURER')")
    @GetMapping("/income")
    public String showIncomeForm(final Model model) {
        log.debug("Showing income entry form");
        return "accountings/detail-income";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BOARD_MEMBER')")
    @Transactional(rollbackOn = Exception.class)
    @PostMapping("/save-income")
    public String saveIncome(
            @RequestParam String transactionDate,
            @RequestParam BigDecimal transactionAmount,
            @RequestParam(required = false) String description,
            RedirectAttributes redirectAttributes,
            Model model) {

        log.debug("Saving manual income Transaction: amount={}", transactionAmount);

        if (description == null || description.isBlank()) {
            model.addAttribute("errorMessage", "Eine Beschreibung ist erforderlich.");
            return "accountings/detail-income";
        }

        if (transactionAmount == null || transactionAmount.compareTo(new BigDecimal("0.01")) < 0) {
            model.addAttribute("errorMessage", "Der Betrag muss mindestens 0,01 € betragen.");
            return "accountings/detail-income";
        }

        LocalDate parsedTransactionDateIncome = LocalDate.parse(transactionDate);
        if (parsedTransactionDateIncome.isAfter(LocalDate.now())) {
            model.addAttribute("errorMessage", "Das Transaktionsdatum darf nicht in der Zukunft liegen.");
            return "accountings/detail-income";
        }

        Transaction tx = new Transaction();
        tx.setTransactionType(TransactionType.INCOME.name());
        tx.setTransactionDate(parsedTransactionDateIncome);
        tx.setAmount(transactionAmount);
        tx.setDescription(description.trim());

        transactionRepository.save(tx);

        log.debug("Income Transaction saved with ID: {}", tx.getId());

        redirectAttributes.addFlashAttribute("successMessage", "Einnahme wurde gespeichert.");
        return "redirect:/accountings/view-transaction/" + tx.getId();
    }


    // --------------------
    // EXPENSE FORM
    // --------------------

    @PreAuthorize("hasAnyRole('ADMIN', 'BOARD_MEMBER', 'TREASURER')")
    @GetMapping("/expense")
    public String showExpenseForm(final Model model) {
        log.debug("Showing expense entry form");
        return "accountings/detail-expense";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BOARD_MEMBER')")
    @Transactional(rollbackOn = Exception.class)
    @PostMapping("/save-expense")
    public String saveExpense(
            @RequestParam String transactionDate,
            @RequestParam BigDecimal transactionAmount,
            @RequestParam(required = false) String description,
            RedirectAttributes redirectAttributes,
            Model model) {

        log.debug("Saving manual expense Transaction: amount={}", transactionAmount);

        if (description == null || description.isBlank()) {
            model.addAttribute("errorMessage", "Eine Beschreibung ist erforderlich.");
            return "accountings/detail-expense";
        }

        if (transactionAmount == null || transactionAmount.compareTo(new BigDecimal("0.01")) < 0) {
            model.addAttribute("errorMessage", "Der Betrag muss mindestens 0,01 € betragen.");
            return "accountings/detail-expense";
        }

        LocalDate parsedTransactionDateExpense = LocalDate.parse(transactionDate);
        if (parsedTransactionDateExpense.isAfter(LocalDate.now())) {
            model.addAttribute("errorMessage", "Das Transaktionsdatum darf nicht in der Zukunft liegen.");
            return "accountings/detail-expense";
        }

        Transaction tx = new Transaction();
        tx.setTransactionType(TransactionType.EXPENSE.name());
        tx.setTransactionDate(parsedTransactionDateExpense);
        tx.setAmount(transactionAmount);
        tx.setDescription(description.trim());

        transactionRepository.save(tx);

        log.debug("Expense Transaction saved with ID: {}", tx.getId());

        redirectAttributes.addFlashAttribute("successMessage", "Ausgabe wurde gespeichert.");
        return "redirect:/accountings/view-transaction/" + tx.getId();
    }


    // --------------------
    // MISC UNACCOUNTED TRANSACTIONS
    // --------------------

    @PreAuthorize("hasAnyRole('ADMIN', 'TREASURER', 'BOARD_MEMBER', 'AUDITOR')")
    @GetMapping("/misc-unaccounted")
    public String listMiscUnaccounted(
            @RequestParam(required = false) String type,
            final Model model) {

        List<Transaction> transactions;
        if (type != null && !type.isBlank()) {
            transactions = transactionRepository.findUnaccountedByType(type);
        } else {
            transactions = transactionRepository.findAllUnaccounted();
        }

        model.addAttribute("transactions", transactions);
        model.addAttribute("selectedType", type != null ? type : "");
        log.debug("Listing {} unaccounted misc transactions (type filter={})", transactions.size(), type);
        return "accountings/list-unaccounted-transactions";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TREASURER')")
    @GetMapping("/book-transaction/{id}")
    public String showBookTransactionForm(final Model model, @PathVariable Long id) {
        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found with id: " + id));
        model.addAttribute("transaction", tx);
        log.debug("Showing booking form for Transaction: {}", id);
        return "accountings/book-transaction";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TREASURER')")
    @Transactional(rollbackOn = Exception.class)
    @PostMapping("/save-transaction-booking")
    public String saveTransactionBooking(
            @RequestParam Long transactionId,
            @RequestParam String accountingDate,
            RedirectAttributes redirectAttributes,
            Model model) {

        Transaction tx = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found with id: " + transactionId));

        if (accountingDate == null || accountingDate.isBlank()) {
            model.addAttribute("errorMessage", "Das Buchungsdatum ist erforderlich.");
            model.addAttribute("transaction", tx);
            return "accountings/book-transaction";
        }

        LocalDate parsedAccountingDate = LocalDate.parse(accountingDate);
        if (parsedAccountingDate.isAfter(LocalDate.now())) {
            model.addAttribute("errorMessage", "Das Buchungsdatum darf nicht in der Zukunft liegen.");
            model.addAttribute("transaction", tx);
            return "accountings/book-transaction";
        }

        AccountingEntry entry = new AccountingEntry();
        entry.setAccountableName(tx.getAccountableName());
        entry.setAccountableId(tx.getAccountableId());
        entry.setTransactionType(tx.getTransactionType());
        entry.setTransactionDate(tx.getTransactionDate());
        entry.setTransactionAmount(tx.getTransactionAmount());
        entry.setAccountingDate(parsedAccountingDate);
        entry.setDescription(tx.getDescription());

        accountingRepository.save(entry);
        tx.setAccountedBy(entry);

        log.debug("Transaction {} booked as AccountingEntry {}", tx.getId(), entry.getId());

        redirectAttributes.addFlashAttribute("successMessage",
                "Buchung für '" + tx.getDescription() + "' wurde gespeichert.");
        return "redirect:/accountings/view-accounting/" + entry.getId();
    }


    // --------------------
    // VIEW
    // --------------------


    @PreAuthorize("hasAnyRole('ADMIN', 'TREASURER', 'BOARD_MEMBER', 'AUDITOR')")
    @GetMapping("/view-accounting/{id}")
    public String viewAccountingEntry(final Model model, @PathVariable Long id) {

        AccountingEntry accountingEntry = accountingRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Accounting entry not found with id: " + id));

        log.debug("Viewing AccountingEntry: " + accountingEntry.toString());

        model.addAttribute("accountingEntry", accountingEntry);
        return "accountings/view-accounting";
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'TREASURER', 'BOARD_MEMBER', 'AUDITOR')")
    @GetMapping("/view-transaction/{id}")
    public String viewTransaction(final Model model, @PathVariable Long id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found with id: " + id));

        log.debug("Viewing Transaction: {}", transaction);

        model.addAttribute("transaction", transaction);
        return "accountings/view-transaction";
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'TREASURER', 'BOARD_MEMBER', 'AUDITOR')")
    @GetMapping("/show-accountings")
    public String showAccountings(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String accountableName,
            @RequestParam(required = false) String transactionType,
            final Model model) {

        int selectedYear = (year != null) ? year : LocalDate.now().getYear();
        String selectedTransactionType = (transactionType != null && !transactionType.isBlank())
                ? transactionType : TransactionType.INCOME.name();
        String selectedAccountableClass = (accountableName != null && !accountableName.isBlank())
                ? accountableName : "";

        List<AccountingEntry> entries;
        if (selectedAccountableClass.isEmpty()) {
            entries = accountingRepository.findByYearAndTransactionType(selectedYear, selectedTransactionType);
        } else {
            entries = accountingRepository.findByYearAndTransactionTypeAndAccountableClass(
                    selectedYear, selectedTransactionType, selectedAccountableClass);
        }

        BigDecimal total = entries.stream()
                .map(AccountingEntry::getTransactionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<String> distinctClasses = accountingRepository.findDistinctAccountableClasses();

        // Year range: current year down 7 years
        int currentYear = LocalDate.now().getYear();
        List<Integer> years = new java.util.ArrayList<>();
        for (int y = currentYear; y >= currentYear - 7; y--) years.add(y);

        model.addAttribute("entries", entries);
        model.addAttribute("total", total);
        model.addAttribute("distinctAccountableClasses", distinctClasses);
        model.addAttribute("years", years);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("selectedTransactionType", selectedTransactionType);
        model.addAttribute("selectedAccountableClass", selectedAccountableClass);

        return "accountings/list-accountingentries";
    }


    // --------------------
    // CREATE NEW, UPDATE
    // --------------------


    @PreAuthorize("hasAnyRole('ADMIN', 'TREASURER')")
    @PostMapping("/new-accountable")
    public String newAccountable(final Model model, @ModelAttribute @Valid AccountableRowSelectionForm formRowData) {

        log.debug("Preparing AccountingEntry from form data: " + formRowData.toString());

        AccountingEntry accountingEntry = new AccountingEntry();

        accountingEntry.setAccountableName(formRowData.getAccountableName());
        accountingEntry.setAccountableId(formRowData.getAccountableId());
        accountingEntry.setAccountableMember(formRowData.getAccountableMemberId() != null ?
                memberRepository.findById(formRowData.getAccountableMemberId()).orElse(null) : null);
        accountingEntry.setTransactionType(formRowData.getTransactionType());
        accountingEntry.setTransactionDate(formRowData.getTransactionDate());
        accountingEntry.setTransactionAmount(formRowData.getTransactionAmount());


        log.debug("AccountingEntry prepared for booking: " + accountingEntry.toString());

        model.addAttribute("accountingEntry", accountingEntry);

        return "accountings/detail-accountable";
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'TREASURER')")
    @Transactional(rollbackOn = Exception.class)
    @PostMapping("/save-accounting")
    public String saveAccountingEntry(final Model model, @ModelAttribute @Valid AccountingEntry accountingEntry,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        log.debug("Saving AccountingEntry: " + accountingEntry.toString());

        if (bindingResult.hasErrors()) {
            log.debug("Validation errors found: " + bindingResult.getAllErrors().toString());
            model.addAttribute("accountingEntry", accountingEntry);
            redirectAttributes.addFlashAttribute("errorMessage", bindingResult.getAllErrors().toString());
            return "accountings/detail-accountable";
        }

        accountingRepository.save(accountingEntry);

        if (accountingEntry.getAccountableName().equals(NbhConst.TIMECHEQUE_ACCOUNTING_NAME)) {
            TimeCheque tc = timeChequeRepository.findById(accountingEntry.getAccountableId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid TimeCheque ID: " + accountingEntry.getAccountableId()));
            tc.setAccountedBy(accountingEntry);
        }
        else if (accountingEntry.getAccountableName().equals(NbhConst.MEMBERSHIPFEE_ACCOUNTING_NAME)) {
            MembershipFee mf = membershipFeeRepository.findById(accountingEntry.getAccountableId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid MembershipFee ID: " + accountingEntry.getAccountableId()));
            mf.setAccountedBy(accountingEntry);
        }
        else if (accountingEntry.getAccountableName().equals("SOME_OTHER_CLASS")) {
            // TODO: Handle other accountable classes as needed
        }


        log.debug("AccountingEntry saved with ID: " + accountingEntry.getId());

        redirectAttributes.addFlashAttribute("accountingEntry", accountingEntry);
        redirectAttributes.addFlashAttribute("successMessage", "Buchung für " + accountingEntry.getAccountableName() + " wurde gespeichert.");
        return "redirect:/accountings/view-accounting/" + accountingEntry.getId();
    }


}
