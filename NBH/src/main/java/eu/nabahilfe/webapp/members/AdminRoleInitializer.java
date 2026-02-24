package eu.nabahilfe.webapp.members;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import eu.nabahilfe.webapp.NbhConst;

/**
 * Ensures the built-in Administrator role exists at application startup.
 */
@Component
public class AdminRoleInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminRoleInitializer.class);

    private final RoleRepository roleRepository;

    public AdminRoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        roleRepository.findByRoleNameIgnoreCase(NbhConst.ADMIN_ROLE_NAME).ifPresentOrElse(
                role -> log.debug(NbhConst.ADMIN_ROLE_NAME + " role already present with id={}", role.getId()),
                this::createAdministratorRole);
    }

    private void createAdministratorRole() {
        Role admin = new Role();
        admin.setRoleName(NbhConst.ADMIN_ROLE_NAME);
        admin.setIsAdmin(true);
        // Satisfy validation rule that Admin must carry a Vereinsrolle
        admin.setIsBoardMember(false);
        admin.setIsTreasurer(false);
        admin.setIsSecretary(false);
        admin.setIsAuditor(false);
        admin.setIsTimeKeeper(false);
        admin.setIsMiscellaneous(false);
        roleRepository.save(admin);
        log.info(NbhConst.ADMIN_ROLE_NAME + " role created at startup");
    }
}
