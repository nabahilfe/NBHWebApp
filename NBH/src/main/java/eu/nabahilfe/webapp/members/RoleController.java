/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.members;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import eu.nabahilfe.webapp.NbhConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/roles")
public class RoleController {

    private final RoleRepository roleRepository;

    private static final Logger log = LoggerFactory.getLogger(RoleController.class);

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    // --------------------
    // mapping model attribute 'role' to fetch Role by id or create new one
    // --------------------

    @PreAuthorize("hasRole('USER')")
    @ModelAttribute("role")
    public Role findRole(@PathVariable(required = false) Long id) {
        return id == null ? new Role() : roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + id
                        + ". Please ensure the ID is correct and the role exists in the database."));
    }


    // --------------------
    // LIST & DETAIL
    // --------------------

    @PreAuthorize("hasAnyRole('ADMIN', 'EXECUTIVE_MEMBER', 'TREASURER', 'SECRETARY', 'TIME_KEEPER')")
    @GetMapping
    String listAllRoles(final Model model) {
        List<Role> roles = roleRepository.findAllBy(Sort.by(Role::getRoleName));
        model.addAttribute("roles", roles);
        log.debug("Listing all Roles, count: {}", roles.size());
        return "roles/list-roles";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    String editRole(final Model model, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Rolle mit ID " + id + " wurde nicht gefunden.");
            return "redirect:/roles";
        }
        if (NbhConst.ADMIN_ROLE_NAME.equalsIgnoreCase(role.get().getRoleName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Die Rolle '" + NbhConst.ADMIN_ROLE_NAME + "' kann nicht geändert werden.");
            return "redirect:/roles";
        }
        model.addAttribute("role", role.get());
        log.debug("Editing Role: {}", role.get());
        return "roles/detail-role";
    }


    // --------------------
    // CREATE NEW, UPDATE
    // --------------------

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    String newRole(final Model model) {
        log.debug("Creating new Role: {}", model.getAttribute("role"));
        return "roles/detail-role";
    }



    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(rollbackOn = Exception.class)
    @PostMapping
    String saveRole(Model model, @ModelAttribute @Valid Role role,
            RedirectAttributes redirectAttributes, BindingResult result, HttpServletRequest request) {

        boolean isPersistedAdmin = role.getId() != null && roleRepository.findById(role.getId())
                .map(r -> NbhConst.ADMIN_ROLE_NAME.equalsIgnoreCase(r.getRoleName()))
                .orElse(false);

        if (isPersistedAdmin || NbhConst.ADMIN_ROLE_NAME.equalsIgnoreCase(role.getRoleName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Die Rolle '" + NbhConst.ADMIN_ROLE_NAME + "' kann nicht geändert werden.");
            return "redirect:/roles";
        }

        String roleError = validateRoleAttributes(role);
        if (roleError != null) {
            model.addAttribute("errorMessage", roleError);
            log.info("Role Validation failed for: {} with error: {}", role, roleError);
            return "roles/detail-role";
        }

        roleRepository.save(role);
        redirectAttributes.addFlashAttribute("successMessage", "Rolle '" + role.getRoleName() + "' wurde gespeichert.");
        log.debug("Role saved: {}", role);
        return "redirect:/roles";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}")
    public String updateRole(Model model, @ModelAttribute @Valid Role role,
            RedirectAttributes redirectAttributes, BindingResult result, HttpServletRequest request, @PathVariable Long id) {
        log.debug("Update Role with id {}: {}", id, role);
        return saveRole(model, role, redirectAttributes, result, request);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    @Transactional(rollbackOn = Exception.class)

    String deleteRole(Model model, @PathVariable Long id, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        Optional<Role> role = roleRepository.findById(id);
        if (role.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Rolle mit ID " + id + " wurde nicht gefunden.");
            return "redirect:/roles";
        }
        if (NbhConst.ADMIN_ROLE_NAME.equalsIgnoreCase(role.get().getRoleName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Die Rolle '" + NbhConst.ADMIN_ROLE_NAME + "' kann nicht gelöscht werden.");
            return "redirect:/roles";
        }
        roleRepository.delete(role.get());
        redirectAttributes.addFlashAttribute("successMessage", "Rolle " + role.get().getRoleName() + " wurde gelöscht.");
        log.debug("Role with id {} deleted.", id);
        return "redirect:/roles";
    }


    // --------------------
    // Other Functions
    // --------------------

    // FIXME : Add actions to table header to enable sorting - see how it is done in list-members
    @PreAuthorize("hasAnyRole('ADMIN', 'EXECUTIVE_MEMBER', 'TREASURER', 'SECRETARY', 'TIME_KEEPER')")
    @GetMapping("/sort/{sortField}")
    String listAll(final Model model, @PathVariable String sortField) {
        List<Role> roles = roleRepository.findAllBy(Sort.by(sortField).descending());
        model.addAttribute("roles", roles);
        model.addAttribute("name", "Max!");
        return "roles/list-roles";
    }



    // --------------------
    // do the validation
    // --------------------

    /*
        Validation Rules:
            1) Auditor und Miscellaneous dürfen keine andere Rolle haben
            2) VEREINSROLLEN BoardMember, Treasurer, Secretary, Auditor können NICHT kombiniert werden
            3) ZUSATZ-ROLLE Admin und TimeKeeper müssen eine der Vereinsrollen BoardMember, Treasurer, Secretary haben
            4) Admin nicht mit TimeKeeper kombinieren, da Admin alle Rechte hat und TimeKeeper nur Zeit-Schecks vergeben kann

        isExecutiveMember       Bool not null    "VEREINSROLLE - Vorstand"
        isTreasurer         Bool not null    "VEREINSROLLE - Kassier, Verwaltet die Buchungen"
        isSecretary         Bool not null    "VEREINSROLLE - Schriftführer"
        isAuditor           Bool not null    "VEREINSROLLE - Rechnungsprüfer, muss unabhängig vom Vorstand sein, darf keine sonstige Rollen haben"
        isTimeKeeper        Bool not null    "ZUSATZ-ROLLE - Kann Zeit-Schecks vergeben / verkaufen / verbuchen, muss Vereinsrolle haben"
        isAdmin             Bool not null    "ZUSATZ-ROLLE - Hat alle Rechte, muss eine Vereinsrollen haben"
        isMiscellaneous     Bool not null    "SPEZIAL-ROLLE - z.B. Ehrenmitglied"
     */

    private String validateRoleAttributes(Role role) {

        // Check rule 1: Auditor und Miscellaneous dürfen keine andere Rolle haben

        if (role.getIsAuditor() && (role.getIsExecutiveMember() || role.getIsTreasurer() || role.getIsSecretary() || role.getIsTimeKeeper() || role.getIsAdmin() || role.getIsMiscellaneous()))
            return "Rolle mit Funktion 'Rechnungsrüfer' darf keine andere Funktion haben!";

        if (role.getIsMiscellaneous() && (role.getIsExecutiveMember() || role.getIsTreasurer() || role.getIsSecretary() || role.getIsAuditor() || role.getIsTimeKeeper() || role.getIsAdmin()))
            return "Rolle mit Funktion 'Sonsiges' darf keine andere Funktion haben!";

        // Check rule 2: VEREINSROLLEN BoardMember, Treasurer, Secretary, Auditor können NICHT kombiniert werden

        int clubRoleCount = 0;
        if (role.getIsExecutiveMember()) clubRoleCount++;
        if (role.getIsTreasurer()) clubRoleCount++;
        if (role.getIsSecretary()) clubRoleCount++;
        if (role.getIsAuditor()) clubRoleCount++;
        if (clubRoleCount > 1)
            return "Es darf nur eine Vereinsrolle (Vorstand, Kassier, Schriftführer, Rechnungsprüfer) ausgewählt werden!";

        // Check rule 3: ZUSATZ-ROLLE Admin und TimeKeeper müssen eine der Vereinsrollen BoardMember, Treasurer, Secretary haben
        if ((role.getIsTimeKeeper()) && !(role.getIsExecutiveMember() || role.getIsTreasurer() || role.getIsSecretary()))
            return "Rollen mit Funktion 'Zeitschecks' müssen eine der Vereinsrollen Vorstand, Kassier oder Schriftführer haben!";

        // Check rule 4: Admin muss mit mindestens einer Vereinsrolle (ausser Auditor) kombiniert werden, da Admin alle Rechte hat und dshalb im Vorstand sein muss
        if (role.getIsAdmin() && !(role.getIsExecutiveMember() || role.getIsTreasurer() || role.getIsSecretary()))
            return "Rollen mit Funktion 'Administrator' müssen mit mindestens einer Vereinsrolle (Vorstand, Kassier, Schriftführer) kombiniert werden!";

        // Check rule 5: Es können keine Rollen ohne Funktion angelegt werden! Bitte wählen Sie mindestens eine Funktion aus.
        int otherRoleCount = 0;
        if (role.getIsAdmin()) otherRoleCount++;
        if (role.getIsTimeKeeper()) otherRoleCount++;
        if (role.getIsMiscellaneous()) otherRoleCount++;
        if (otherRoleCount + clubRoleCount == 0)
            return "Es können keine Rollen ohne Funktion angelegt werden! Bitte wählen Sie mindestens eine Funktion aus.";

        return null;
    }


}