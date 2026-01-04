package eu.nabahilfe.webapp.members;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.ListCrudRepository;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends ListCrudRepository<Member, Long> {

    Page<Member> findAllByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(
            String lastName, String firstName, Pageable pageable);

    List<Member> findAllByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrStreetContainingIgnoreCase(
            String lastName, String firstName, String street);

    List<Member> findAllByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(
            String lastName, String firstName);

    List<Member> findAllBy(Sort sort);

    Page<Member> findAll(Pageable pageable);

    Optional<Member> findTopByOrderByMemberNmbrDesc();

}
