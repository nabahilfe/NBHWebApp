package eu.nabahilfe.webapp.members;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.ListCrudRepository;


public interface RoleRepository extends ListCrudRepository<Role, Long> {

    List<Role> findByIsBoardMember(Boolean isBoardMember);

    List<Role>findByIsAdmin(Boolean isAdmin);

    List<Role> findAllByRoleNameContainingIgnoreCase(String roleName);

    List<Role> findAllBy(Sort sort);

    Optional<Role> findByRoleNameIgnoreCase(String roleName);
}