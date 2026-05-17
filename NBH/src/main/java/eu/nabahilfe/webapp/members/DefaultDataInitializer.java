/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.members;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import eu.nabahilfe.webapp.NbhConst;
import eu.nabahilfe.webapp.org.Offer;
import eu.nabahilfe.webapp.org.OfferRepository;

/**
 * Ensures the built-in System-Administrator role and member exist at application startup.
 */
@Component
@Order(1)
public class DefaultDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DefaultDataInitializer.class);

    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;
    private final OfferRepository offerRepository;

    
    public DefaultDataInitializer(RoleRepository roleRepository, MemberRepository memberRepository, OfferRepository offerRepository) {
        this.roleRepository = roleRepository;
        this.memberRepository = memberRepository;	
        this.offerRepository = offerRepository;
    }

    
    @Override
    public void run(String... args) {
        Role adminRole = ensureAdminRoleExists();
        ensureAdminMemberExists(adminRole);
        ensureDefaultRolesExists();
        ensureOffersExist();
    }

    private void ensureOffersExist() {
		if (offerRepository.count() > 0) {
			log.debug("Offers already exist, skipping default offer creation");
			return;
		}

		Offer offer = null;
		Offer saved = null;
		
		// '100','Erfahrungsaustausch und Gespräche'	
		offer = new Offer();
		offer.setCode("100");
		offer.setDescription("Erfahrungsaustausch und Gespräche");
		saved = offerRepository.save(offer);
		log.info("Offer {} - {} created at startup", saved.getCode(), saved.getDescription());		
		
		
		// '200','Alltägliche Hilfsdienste'
		offer = new Offer();
		offer.setCode("200");
		offer.setDescription("Alltägliche Hilfsdienste");
		saved = offerRepository.save(offer);
		log.info("Offer {} - {} created at startup", saved.getCode(), saved.getDescription());		
		
		
		
		// '300','Initiieren und Organisieren von Freizeitaktivitäten'
		offer = new Offer();
		offer.setCode("300");
		offer.setDescription("Initiieren und Organisieren von Freizeitaktivitäten");
		saved = offerRepository.save(offer);
		log.info("Offer {} - {} created at startup", saved.getCode(), saved.getDescription());		
		
		
		
		// '400','Unterstützung bei Formularen sowie Behördenkontakten'
		offer = new Offer();
		offer.setCode("400");
		offer.setDescription("Unterstützung bei Formularen sowie Behördenkontakten");
		saved = offerRepository.save(offer);
		log.info("Offer {} - {} created at startup", saved.getCode(), saved.getDescription());		
		
		
		
		// '500','Transport und Fahrtendienste'
		offer = new Offer();
		offer.setCode("500");
		offer.setDescription("Transport und Fahrtendienste");
		saved = offerRepository.save(offer);
		log.info("Offer {} - {} created at startup", saved.getCode(), saved.getDescription());		
		
		
		
		// '600','Leih-Oma / Leih-Opa'
		offer = new Offer();
		offer.setCode("600");
		offer.setDescription("Leih-Oma / Leih-Opa");
		saved = offerRepository.save(offer);
		log.info("Offer {} - {} created at startup", saved.getCode(), saved.getDescription());		
		
		
		
		// '700','Kleinere Außen- oder Reparaturarbeiten'
		offer = new Offer();
		offer.setCode("700");
		offer.setDescription("Kleinere Außen- oder Reparaturarbeiten");
		saved = offerRepository.save(offer);
		log.info("Offer {} - {} created at startup", saved.getCode(), saved.getDescription());		
		
		
		
		// '800','Hilfe beim Bedienen technischer Geräte und Computer'
		offer = new Offer();
		offer.setCode("800");
		offer.setDescription("Hilfe beim Bedienen technischer Geräte und Computer");
		saved = offerRepository.save(offer);
		log.info("Offer {} - {} created at startup", saved.getCode(), saved.getDescription());		

		
		// '900','Sonstiges - bitte Beschreibung angeben!'
		offer = new Offer();
		offer.setCode("900");
		offer.setDescription("Sonstiges - bitte Beschreibung angeben!");
		saved = offerRepository.save(offer);
		log.info("Offer {} - {} created at startup", saved.getCode(), saved.getDescription());		
		
		
		// '950','Spende von Stunden'
		offer = new Offer();
		offer.setCode("950");
		offer.setDescription("Spende von Stunden");
		saved = offerRepository.save(offer);
		log.info("Offer {} - {} created at startup", saved.getCode(), saved.getDescription());		
		
		
		// '999','Korrekturbuchung'
		offer = new Offer();
		offer.setCode("999");
		offer.setDescription("Korrekturbuchung");
		saved = offerRepository.save(offer);
		log.info("Offer {} - {} created at startup", saved.getCode(), saved.getDescription());		
	}

    
	private void ensureDefaultRolesExists() {
        Role existing = roleRepository.findByRoleNameIgnoreCase("Obmann").orElse(null);
        if (existing != null) {
            log.debug("RoleNames already exist, skipping default role creation");
            return;
        }

		Role role = null;
		Role saved = null;

        // Obfrau
		role = new Role();
		role.setRoleName("Obfrau");
		role.setIsBoardMember(true);
		role.setIsTreasurer(false);
		role.setIsSecretary(false);
		role.setIsAuditor(false);
		role.setIsTimeKeeper(true);
		role.setIsAdmin(false);
		role.setIsMiscellaneous(false);
		saved = roleRepository.save(role);
		log.info("role {} created at startup", saved.getRoleName());
		
        // Obfrau Stv.
		role = new Role();
		role.setRoleName("Obfrau Stv.");
		role.setIsBoardMember(true);
		role.setIsTreasurer(false);
		role.setIsSecretary(false);
		role.setIsAuditor(false);
		role.setIsTimeKeeper(true);
		role.setIsAdmin(true);
		role.setIsMiscellaneous(false);
		saved = roleRepository.save(role);
		log.info("role {} created at startup", saved.getRoleName());
		
		

        // Obmann
		role = new Role();
		role.setRoleName("Obmann");
		role.setIsBoardMember(true);
		role.setIsTreasurer(false);
		role.setIsSecretary(false);
		role.setIsAuditor(false);
		role.setIsTimeKeeper(true);
		role.setIsAdmin(false);
		role.setIsMiscellaneous(false);
		saved = roleRepository.save(role);
		log.info("role {} created at startup", saved.getRoleName());
		
        // Obmann Stv.
		role = new Role();
		role.setRoleName("Obmann Stv.");
		role.setIsBoardMember(true);
		role.setIsTreasurer(false);
		role.setIsSecretary(false);
		role.setIsAuditor(false);
		role.setIsTimeKeeper(true);
		role.setIsAdmin(true);
		role.setIsMiscellaneous(false);
		saved = roleRepository.save(role);
		log.info("role {} created at startup", saved.getRoleName());
		
		
				
        // Kassier	
		role = new Role();
		role.setRoleName("Kassier");
		role.setIsBoardMember(false);
		role.setIsTreasurer(true);
		role.setIsSecretary(false);
		role.setIsAuditor(false);
		role.setIsTimeKeeper(false);
		role.setIsAdmin(false);
		role.setIsMiscellaneous(false);
		saved = roleRepository.save(role);
		log.info("role {} created at startup", saved.getRoleName());
		
        // Kassier Stv.
		role = new Role();
		role.setRoleName("Kassier Stv.");
		role.setIsBoardMember(false);
		role.setIsTreasurer(true);
		role.setIsSecretary(false);
		role.setIsAuditor(false);
		role.setIsTimeKeeper(false);
		role.setIsAdmin(false);
		role.setIsMiscellaneous(false);
		saved = roleRepository.save(role);
		log.info("role {} created at startup", saved.getRoleName());

		

        // Rechnungsprüfer	
		role = new Role();
		role.setRoleName("Rechnungsprüfer");
		role.setIsBoardMember(false);
		role.setIsTreasurer(false);
		role.setIsSecretary(false);
		role.setIsAuditor(true);
		role.setIsTimeKeeper(false);
		role.setIsAdmin(false);
		role.setIsMiscellaneous(false);
		saved = roleRepository.save(role);
		log.info("role {} created at startup", saved.getRoleName());
		
        // Rechnungsprüfer Stv.
		role = new Role();
		role.setRoleName("Rechnungsprüfer Stv.");
		role.setIsBoardMember(false);
		role.setIsTreasurer(false);
		role.setIsSecretary(false);
		role.setIsAuditor(true);
		role.setIsTimeKeeper(false);
		role.setIsAdmin(false);
		role.setIsMiscellaneous(false);
		saved = roleRepository.save(role);
		log.info("role {} created at startup", saved.getRoleName());
		
		
		
        // Schriftführer	
		role = new Role();
		role.setRoleName("Schriftführer");
		role.setIsBoardMember(false);
		role.setIsTreasurer(false);
		role.setIsSecretary(true);
		role.setIsAuditor(false);
		role.setIsTimeKeeper(false);
		role.setIsAdmin(false);
		role.setIsMiscellaneous(false);
		saved = roleRepository.save(role);
		log.info("role {} created at startup", saved.getRoleName());
		
        // Schriftführer Stv.
		role = new Role();
		role.setRoleName("Schriftführer Stv.");
		role.setIsBoardMember(false);
		role.setIsTreasurer(false);
		role.setIsSecretary(true);
		role.setIsAuditor(false);
		role.setIsTimeKeeper(false);
		role.setIsAdmin(false);
		role.setIsMiscellaneous(false);
		saved = roleRepository.save(role);
		log.info("role {} created at startup", saved.getRoleName());
		
		
        // Ehrenmitglied	
		role = new Role();
		role.setRoleName("Ehrenmitglied");
		role.setIsBoardMember(false);
		role.setIsTreasurer(false);
		role.setIsSecretary(false);
		role.setIsAuditor(false);
		role.setIsTimeKeeper(false);
		role.setIsAdmin(false);
		role.setIsMiscellaneous(true);
		saved = roleRepository.save(role);
		log.info("role {} created at startup", saved.getRoleName());
		
		
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
            log.info("{} role created at startup", saved.getRoleName());
            return saved;
        });
    }

    private void ensureAdminMemberExists(Role adminRole) {
        Member existing = memberRepository.findByEmail(NbhConst.ADMIN_EMAIL);
        if (existing != null) {
            log.debug("System Administrator member already present with id={}", existing.getId());
            return;
        }

        Member admin = new Member();
        admin.setFirstName(NbhConst.ADMIN_ACCOUNT_FIRST_NAME);
        admin.setLastName(NbhConst.ADMIN_ACCOUNT_LAST_NAME);
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
        log.info("System Administrator member created at startup with email={}", NbhConst.ADMIN_EMAIL);
    }

    private Integer getNextMemberNumber() {
        return memberRepository.findTopByOrderByMemberNmbrDesc()
                .map(m -> m.getMemberNmbr() + 1)
                .orElse(NbhConst.START_MEMBER_NUMBER);
    }
}
