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
            RedirectAttributes redirectAttributes, BindingResult result) {

        String roleError = validateRoleAttributes(role);
        if (roleError != null) {
            model.addAttribute("errorMessage", roleError);
            log.info("Role Validation failed for: {} with error: {}", role, roleError);
            return "roles/detail-role";
        }

        roleRepository.save(role);
        redirectAttributes.addFlashAttribute("successMessage", "Rolle '" + role.getRoleName() + "' wurde gespeichert.");
        log.debug("Role saved: {}", role);
        return "redirect:/roles/" + role.getId();
    }


    @PostMapping("/{id}")
    public String updateRole(Model model, @ModelAttribute @Valid Role role,
            RedirectAttributes redirectAttributes, BindingResult result, @PathVariable Long id) {
        log.debug("Update Role with id {}: {}", id, role);
        return saveRole(model, role, redirectAttributes, result);
    }


    // --------------------
    // DELETE
    // --------------------

    @PostMapping("/delete/{id}")
    @Transactional
    String deleteRole(Model model, @PathVariable Long id, RedirectAttributes redirectAttributes) {
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

    private String validateRoleAttributes(Role role) {

        if (role.getIsAuditor() && (role.getIsAdmin() || role.getIsBoardMember() || role.getIsTimeKeeper() || role.getIsMiscellaneous() || role.getIsTreasurer() || role.getIsSecretary()))
            return "Rolle mit Zuordnung 'Rechnungsrüfer' darf keine andere Zuordnung haben!";

        if (role.getIsMiscellaneous() && (role.getIsAdmin() || role.getIsBoardMember() || role.getIsTimeKeeper() || role.getIsAuditor() || role.getIsTreasurer() || role.getIsSecretary()))
            return "Rolle mit Zuordnung 'Sonsiges' darf keine andere Zuordnung haben!";

        if (!(role.getIsAdmin() || role.getIsAuditor() || role.getIsBoardMember() || role.getIsTimeKeeper() || role.getIsMiscellaneous() || role.getIsTreasurer() || role.getIsSecretary()))
            return "Es muss mindestens eine Zuordnung für die Rolle ausgewählt werden!";

        return null;
    }


}
