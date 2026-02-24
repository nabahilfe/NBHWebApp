package eu.nabahilfe.webapp.members;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.ListCrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @EntityGraph(attributePaths = "role")
    @org.springframework.data.jpa.repository.Query("select m from Member m where m.birthdate is not null and MONTH(m.birthdate) = :month order by m.birthdate asc")
    List<Member> findByBirthMonth(@org.springframework.data.repository.query.Param("month") int month);

    default List<MemberBirthdayForm> findBirthdaysByMonthOffset(int monthOffset) {
        LocalDate target = LocalDate.now().plusMonths(monthOffset);
        int targetMonth = target.getMonthValue();
        int targetYear = target.getYear();

        return findByBirthMonth(targetMonth).stream()
                .filter(m -> m.getBirthdate() != null)
                .map(m -> new MemberBirthdayForm(
                        m.getFirstName(),
                        m.getLastName(),
                        m.getBirthdate(),
                        targetYear - m.getBirthdate().getYear()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    Member findByEmail(String email);

    List<Member> findBySalutation(String salutation);

    @EntityGraph(attributePaths = "role")
    @org.springframework.data.jpa.repository.Query(
        "SELECT m FROM Member m JOIN m.role r " +
        "WHERE r.isBoardMember = true OR r.isTreasurer = true OR r.isSecretary = true " +
        "ORDER BY r.isBoardMember DESC, r.isTreasurer DESC, r.isSecretary DESC")
    List<Member> findBoardMembers();

}