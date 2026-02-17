package eu.nabahilfe.webapp.members;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.ListCrudRepository;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends ListCrudRepository<Member, Long> {

    @EntityGraph(attributePaths = "role")
    Page<Member> findAllByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(
            String lastName, String firstName, Pageable pageable);

    @EntityGraph(attributePaths = "role")
    List<Member> findAllByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrStreetContainingIgnoreCase(
            String lastName, String firstName, String street);

    @EntityGraph(attributePaths = "role")
    List<Member> findAllByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(
            String lastName, String firstName);

    @EntityGraph(attributePaths = "role")
    List<Member> findAllBy(Sort sort);

    @EntityGraph(attributePaths = "role")
    Page<Member> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "role")
    Optional<Member> findTopByOrderByMemberNmbrDesc();


    Member findByEmail(String email);

    List<Member> findBySalutation(String salutation);

}
