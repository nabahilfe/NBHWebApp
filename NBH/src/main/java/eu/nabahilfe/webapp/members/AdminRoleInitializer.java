package eu.nabahilfe.webapp.members;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import eu.nabahilfe.webapp.NbhConst;

/**
 * Ensures the built-in System-Administrator role and member exist at application startup.
 */
@Component
@Order(1)
public class AdminRoleInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminRoleInitializer.class);

    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;

    public AdminRoleInitializer(RoleRepository roleRepository, MemberRepository memberRepository) {
        this.roleRepository = roleRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) {
        Role adminRole = ensureAdminRoleExists();
        ensureAdminMemberExists(adminRole);
    }

    private Role ensureAdminRoleExists() {
        return roleRepository.findByRoleNameIgnoreCase(NbhConst.ADMIN_ROLE_NAME).orElseGet(() -> {
            Role admin = new Role();
            admin.setRoleName(NbhConst.ADMIN_ROLE_NAME);
            admin.setIsAdmin(true);
            admin.setIsBoardMember(false);
            admin.setIsTreasurer(false);
            admin.setIsSecretary(false);
            admin.setIsAuditor(false);
            admin.setIsTimeKeeper(false);
            admin.setIsMiscellaneous(false);
            Role saved = roleRepository.save(admin);
            log.info("{} role created at startup", NbhConst.ADMIN_ROLE_NAME);
            return saved;
        });
    }

    private void ensureAdminMemberExists(Role adminRole) {
        Member existing = memberRepository.findByEmail(NbhConst.ADMIN_EMAIL);
        if (existing != null) {
            log.debug("System-Administrator member already present with id={}", existing.getId());
            return;
        }

        Member admin = new Member();
        admin.setFirstName("System");
        admin.setLastName("Administrator");
        admin.setBirthdate(LocalDate.of(2000, 1, 1));
        admin.setEmail(NbhConst.ADMIN_EMAIL);
        admin.setStreet("na");
        admin.setNumber("na");
        admin.setZip("na");
        admin.setCity("na");
        admin.setRole(adminRole);
        admin.setJoiningDate(LocalDate.now());
        admin.setDirectDebitAuthorization(false);
        admin.setIsImportedMember(false);
        admin.setAccumulatedHours(0);
        admin.setMemberNmbr(getNextMemberNumber());

        memberRepository.save(admin);
        log.info("System-Administrator member created at startup with email={}", NbhConst.ADMIN_EMAIL);
    }

    private Integer getNextMemberNumber() {
        return memberRepository.findTopByOrderByMemberNmbrDesc()
                .map(m -> m.getMemberNmbr() + 1)
                .orElse(NbhConst.START_MEMBER_NUMBER);
    }
}
