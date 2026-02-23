package eu.nabahilfe.webapp.members;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @ModelAttribute("role")
    public Role findRole(@PathVariable(required = false) Long id) {
        return id == null ? new Role() : roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + id
                        + ". Please ensure the ID is correct and the role exists in the database."));
    }


    // --------------------
    // LIST & DETAIL
    // --------------------

    @GetMapping
    String listAllRoles(final Model model) {
        List<Role> roles = roleRepository.findAllBy(Sort.by("roleName"));
        model.addAttribute("roles", roles);
        log.debug("Listing all Roles, count: {}", roles.size());
        return "roles/list-roles";
    }


    @GetMapping("/{id}")
    String editRole(final Model model, @PathVariable Long id) {
        Optional<Role> role = roleRepository.findById(id);
        model.addAttribute("role", role.get());
        log.debug("Editing Role: {}", role.get());
        return "roles/detail-role";
    }


    // --------------------
    // CREATE NEW, UPDATE
    // --------------------

    @GetMapping("/new")
    String newRole(final Model model) {
        log.debug("Creating new Role: {}", model.getAttribute("role"));
        return "roles/detail-role";
    }


    @Transactional
    @PostMapping
    String saveRole(Model model, @ModelAttribute @Valid Role role,
            RedirectAttributes redirectAttributes, BindingResult result, HttpServletRequest request) {

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


    @PostMapping("/{id}")
    public String updateRole(Model model, @ModelAttribute @Valid Role role,
            RedirectAttributes redirectAttributes, BindingResult result, HttpServletRequest request, @PathVariable Long id) {
        log.debug("Update Role with id {}: {}", id, role);
        return saveRole(model, role, redirectAttributes, result, request);
    }


    @PostMapping("/delete/{id}")
    @Transactional
    String deleteRole(Model model, @PathVariable Long id, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        Optional<Role> role = roleRepository.findById(id);
        roleRepository.delete(role.get());
        redirectAttributes.addFlashAttribute("successMessage", "Rolle " + role.get().getRoleName() + " wurde gelöscht.");
        log.debug("Role with id {} deleted.", id);
        return "redirect:/roles";
    }


    // --------------------
    // Other Functions
    // --------------------

    // FIXME : Add actions to table header to enable sorting - see how it is done in list-members
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

        isBoardMember       Bool not null    "VEREINSROLLE - Vorstand"
        isTreasurer         Bool not null    "VEREINSROLLE - Kassier, Verwaltet die Buchungen"
        isSecretary         Bool not null    "VEREINSROLLE - Schriftführer"
        isAuditor           Bool not null    "VEREINSROLLE - Rechnungsprüfer, muss unabhängig vom Vorstand sein, darf keine sonstige Rollen haben"
        isTimeKeeper        Bool not null    "ZUSATZ-ROLLE - Kann Zeit-Schecks vergeben / verkaufen / verbuchen, muss Vereinsrolle haben"
        isAdmin             Bool not null    "ZUSATZ-ROLLE - Hat alle Rechte, muss eine Vereinsrollen haben"
        isMiscellaneous     Bool not null    "SPEZIAL-ROLLE - z.B. Ehrenmitglied"
     */

    private String validateRoleAttributes(Role role) {

        // Check rule 1: Auditor und Miscellaneous dürfen keine andere Rolle haben

        if (role.getIsAuditor() && (role.getIsBoardMember() || role.getIsTreasurer() || role.getIsSecretary() || role.getIsTimeKeeper() || role.getIsAdmin() || role.getIsMiscellaneous()))
            return "Rolle mit Funktion 'Rechnungsrüfer' darf keine andere Funktion haben!";

        if (role.getIsMiscellaneous() && (role.getIsBoardMember() || role.getIsTreasurer() || role.getIsSecretary() || role.getIsAuditor() || role.getIsTimeKeeper() || role.getIsAdmin()))
            return "Rolle mit Funktion 'Sonsiges' darf keine andere Funktion haben!";

        // Check rule 2: VEREINSROLLEN BoardMember, Treasurer, Secretary, Auditor können NICHT kombiniert werden

        int clubRoleCount = 0;
        if (role.getIsBoardMember()) clubRoleCount++;
        if (role.getIsTreasurer()) clubRoleCount++;
        if (role.getIsSecretary()) clubRoleCount++;
        if (role.getIsAuditor()) clubRoleCount++;
        if (clubRoleCount > 1)
            return "Es darf nur eine Vereinsrolle (Vorstand, Kassier, Schriftführer, Rechnungsprüfer) ausgewählt werden!";

        // Check rule 3: ZUSATZ-ROLLE Admin und TimeKeeper müssen eine der Vereinsrollen BoardMember, Treasurer, Secretary haben
        if ((role.getIsAdmin() || role.getIsTimeKeeper()) && !(role.getIsBoardMember() || role.getIsTreasurer() || role.getIsSecretary()))
            return "Rollen mit Funktion 'Administrator' oder 'Zeitschecks' müssen eine der Vereinsrollen Vorstand, Kassier oder Schriftführer haben!";

        // Check rule 4: Admin nicht mit TimeKeeper kombinieren, da Admin alle Rechte hat und TimeKeeper nur Zeit-Schecks vergeben kann
        if (role.getIsAdmin() && role.getIsTimeKeeper())
            return "Rollen mit Funktion 'Administrator' nicht mit Funktion 'Zeitschecks' kombiniert, Administrator hat sowieso auf alles Zugriff.";

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
