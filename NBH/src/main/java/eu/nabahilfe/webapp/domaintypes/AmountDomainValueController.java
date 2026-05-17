/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.domaintypes;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/domaintypes")
@PreAuthorize("hasAnyRole('ADMIN', 'BOARD_MEMBER')")
public class AmountDomainValueController {

    private static final Logger log = LoggerFactory.getLogger(AmountDomainValueController.class);

    private final AmountDomainValueRepository repo;

    public AmountDomainValueController(AmountDomainValueRepository repo) {
        this.repo = repo;
    }


    /* List all, optionally filtered by AmountDomainType */
    @GetMapping
    public String list(Model model, @RequestParam(required = false) String type) {
        List<AmountDomainValue> values;
        if (type != null && !type.isBlank()) {
            values = repo.findByCodeOrderByValidFromDesc(type);
        } else {
            values = repo.findAllByOrderByValidFromDesc();
        }
        // Build a set of IDs that are the latest (highest validFrom) per code and may be deleted
        java.util.Set<Long> deletableIds = new java.util.HashSet<>();
        for (AmountDomainType t : AmountDomainType.values()) {
            repo.findLatestByCode(t.name()).ifPresent(latest -> {
                if (latest.getValidFrom().getYear() > LocalDate.now().getYear()) {
                    deletableIds.add(latest.getId());
                }
            });
        }
        model.addAttribute("amountDomainValues", values);
        model.addAttribute("amountDomainTypes", AmountDomainType.values());
        model.addAttribute("selectedType", type);
        model.addAttribute("deletableIds", deletableIds);
        return "domaintypes/list-domain-values";
    }


    private static final LocalDate OPEN_DATE = LocalDate.of(9999, 12, 31);

    /**
     * Returns the single valid validFrom date for a new record:
     * - Jan 1 of the current year if no previous record exists for that code
     * - Jan 1 of (previous record's validFrom year + 1) if a previous record exists
     */
    private LocalDate determineValidFrom(String code) {
        if (code != null && !code.isBlank()) {
            Optional<AmountDomainValue> prev = repo.findOpenByCode(code, OPEN_DATE);
            if (prev.isPresent()) {
                return LocalDate.of(prev.get().getValidFrom().getYear() + 1, 1, 1);
            }
        }
        return LocalDate.of(LocalDate.now().getYear(), 1, 1);
    }

    private void addFormAttributes(Model model, AmountDomainValue adv) {
        model.addAttribute("amountDomainValue", adv);
        model.addAttribute("amountDomainTypes", AmountDomainType.values());
        if (adv.getId() == null) {
            // Pre-compute the valid validFrom date for every AmountDomainType so the
            // template can update the displayed date via JS when the user selects a code.
            Map<String, String> validFromByCode = new LinkedHashMap<>();
            for (AmountDomainType t : AmountDomainType.values()) {
                validFromByCode.put(t.name(), determineValidFrom(t.name()).toString());
            }
            model.addAttribute("validFromByCode", validFromByCode);
            // Pre-select the date for any code that is already set (e.g. on validation error round-trip)
            model.addAttribute("computedValidFrom", determineValidFrom(adv.getCode()));
        }
    }


    /* Show create form */
    @GetMapping("/create")
    public String createForm(Model model) {
        addFormAttributes(model, new AmountDomainValue());
        return "domaintypes/amount-domain-value-edit";
    }


    private static final String EDIT_FORBIDDEN_MSG =
            "Einträge für das aktuelle oder vergangene Jahre dürfen nicht bearbeitet oder gelöscht werden.";

    private boolean isProtected(AmountDomainValue adv) {
        return adv.getValidFrom().getYear() <= LocalDate.now().getYear();
    }

    /* Show edit form */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<AmountDomainValue> adv = repo.findById(id);
        if (adv.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Eintrag mit ID " + id + " nicht gefunden.");
            return "redirect:/domaintypes";
        }
        if (isProtected(adv.get())) {
            redirectAttributes.addFlashAttribute("errorMessage", EDIT_FORBIDDEN_MSG);
            return "redirect:/domaintypes";
        }
        addFormAttributes(model, adv.get());
        return "domaintypes/amount-domain-value-edit";
    }


    /* Save (create or update) */
    @Transactional
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute AmountDomainValue amountDomainValue, RedirectAttributes redirectAttributes, Model model) {
        boolean isNew = (amountDomainValue.getId() == null);

        if (!isNew) {
            // Guard: existing records for current year or earlier must not be modified
            Optional<AmountDomainValue> existing = repo.findById(amountDomainValue.getId());
            if (existing.isPresent() && isProtected(existing.get())) {
                redirectAttributes.addFlashAttribute("errorMessage", EDIT_FORBIDDEN_MSG);
                return "redirect:/domaintypes";
            }
        }

        if (isNew) {
            // For new entries validTo is always 9999-12-31
            amountDomainValue.setValidTo(OPEN_DATE);

            // Close the previous open record for the same code, if any
            Optional<AmountDomainValue> previousOpen = repo.findOpenByCode(amountDomainValue.getCode(), OPEN_DATE);
            if (previousOpen.isPresent()) {
                AmountDomainValue prev = previousOpen.get();

                // Validate: new validFrom must be Jan 1 of the year after the previous record's year
                LocalDate requiredValidFrom = LocalDate.of(prev.getValidFrom().getYear() + 1, 1, 1);
                if (!amountDomainValue.getValidFrom().equals(requiredValidFrom)) {
                    model.addAttribute("errorMessage",
                            "Das Gültig-ab-Datum muss der 1. Januar " + requiredValidFrom.getYear() + " sein.");
                    addFormAttributes(model, amountDomainValue);
                    return "domaintypes/amount-domain-value-edit";
                }

                // Set validTo to last day of the month before the new validFrom
                LocalDate lastDayOfPrevMonth = amountDomainValue.getValidFrom().minusDays(1);
                prev.setValidTo(lastDayOfPrevMonth);
                repo.save(prev);
                log.debug("Closed previous AmountDomainValue: {}", prev);
            } else {
                // First record: validFrom must be Jan 1 of the current year
                LocalDate requiredValidFrom = LocalDate.of(LocalDate.now().getYear(), 1, 1);
                if (!amountDomainValue.getValidFrom().equals(requiredValidFrom)) {
                    model.addAttribute("errorMessage",
                            "Das Gültig-ab-Datum für den ersten Eintrag muss der 1. Januar " + requiredValidFrom.getYear() + " sein.");
                    addFormAttributes(model, amountDomainValue);
                    return "domaintypes/amount-domain-value-edit";
                }
            }
        }

        repo.save(amountDomainValue);
        log.debug("AmountDomainValue saved: {}", amountDomainValue);
        redirectAttributes.addFlashAttribute("successMessage", "Eintrag wurde gespeichert.");
        return "redirect:/domaintypes";
    }


    /* Delete */
    @Transactional
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<AmountDomainValue> adv = repo.findById(id);
        if (adv.isPresent()) {
            if (isProtected(adv.get())) {
                redirectAttributes.addFlashAttribute("errorMessage", EDIT_FORBIDDEN_MSG);
                return "redirect:/domaintypes";
            }
            // Only the record with the highest validFrom for its code may be deleted
            Optional<AmountDomainValue> latest = repo.findLatestByCode(adv.get().getCode());
            if (latest.isEmpty() || !latest.get().getId().equals(id)) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Nur der neueste Eintrag einer Reihe darf gelöscht werden, um Lücken in der Datenreihe zu vermeiden.");
                return "redirect:/domaintypes";
            }
            repo.delete(adv.get());
            log.debug("AmountDomainValue deleted: {}", adv.get());

            // Re-open the previous record for the same code (set validTo back to 9999-12-31).
            // The predecessor was closed with validTo = deletedRecord.validFrom - 1 day.
            LocalDate predecessorValidTo = adv.get().getValidFrom().minusDays(1);
            repo.findByCodeAndValidTo(adv.get().getCode(), predecessorValidTo)
                .ifPresent(prev -> {
                    prev.setValidTo(OPEN_DATE);
                    repo.save(prev);
                    log.debug("Re-opened previous AmountDomainValue: {}", prev);
                });

            redirectAttributes.addFlashAttribute("successMessage", "Eintrag wurde gelöscht.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Eintrag mit ID " + id + " nicht gefunden.");
        }
        return "redirect:/domaintypes";
    }
}
