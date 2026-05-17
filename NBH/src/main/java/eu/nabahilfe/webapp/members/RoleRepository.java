/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.members;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.ListCrudRepository;


public interface RoleRepository extends ListCrudRepository<Role, Long> {

    List<Role> findByIsBoardMember(Boolean isBoardMember);

    List<Role>findByIsAdmin(Boolean isAdmin);

    List<Role>findByIsTreasurer(Boolean isTreasurer);

    List<Role>findByIsSecretary(Boolean isSecretary);

    List<Role>findByIsAuditor(Boolean isAuditor);

    List<Role> findAllByRoleNameContainingIgnoreCase(String roleName);

    List<Role> findAllBy(Sort sort);

    Optional<Role> findByRoleNameIgnoreCase(String roleName);
}