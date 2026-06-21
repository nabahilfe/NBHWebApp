/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.members;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public interface MemberRepository extends ListCrudRepository<Member, Long> {

    List<Member> findByRole(Role role);

    @EntityGraph(attributePaths = "role")
    @Query("SELECT m FROM Member m " +
           "WHERE (lower(m.lastName) LIKE lower(CONCAT('%', :lastName, '%')) " +
           "   OR lower(m.firstName) LIKE lower(CONCAT('%', :firstName, '%')) " +
           "   OR m.memberNmbr = :memberNmbr) " +
           "AND (m.resignationDate IS NULL OR m.resignationDate > CURRENT_DATE)")
    Page<Member> findAllActiveByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMemberNmbr(
            String lastName, String firstName, Integer memberNmbr, Pageable pageable);

    @EntityGraph(attributePaths = "role")
    List<Member> findAllByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrStreetContainingIgnoreCase(
            String lastName, String firstName, String street);

    @EntityGraph(attributePaths = "role")
    List<Member> findAllByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(
            String lastName, String firstName);

    @EntityGraph(attributePaths = "role")
    @Query("SELECT m FROM Member m " +
            "WHERE (m.resignationDate IS NULL OR m.resignationDate > CURRENT_DATE)")
    Page<Member> findAllActive(Pageable pageable);


    @EntityGraph(attributePaths = "role")
    @Query("SELECT m FROM Member m " +
            "WHERE (m.resignationDate IS NOT NULL AND m.resignationDate <= CURRENT_DATE) " +
            "AND NOT (m.firstName = '*' AND m.lastName = '*')")
    Page<Member> findAllInactive(Pageable pageable);

    @EntityGraph(attributePaths = "role")
    Optional<Member> findTopByOrderByMemberNmbrDesc();

    @EntityGraph(attributePaths = "role")
    @Query("select m from Member m where m.birthdate is not null and MONTH(m.birthdate) = :month order by m.birthdate asc")
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

    @Query("SELECT m FROM Member m WHERE m.id = :id AND lower(m.email) = lower(:email)")
    java.util.Optional<Member> findByIdAndEmail(@org.springframework.data.repository.query.Param("id") Long id,
            @org.springframework.data.repository.query.Param("email") String email);

    List<Member> findBySalutation(String salutation);

    @EntityGraph(attributePaths = "role")
    @Query(
        "SELECT m FROM Member m JOIN m.role r " +
        "WHERE r.isBoardMember = true OR r.isTreasurer = true OR r.isSecretary = true " +
        "ORDER BY m.lastName ASC")
    List<Member> findBoardMembers();

    /** Returns [year, joinedCount] grouped by joiningDate year, ordered ascending */
    @Query("SELECT YEAR(m.joiningDate), COUNT(m) FROM Member m GROUP BY YEAR(m.joiningDate) ORDER BY YEAR(m.joiningDate) ASC")
    List<Object[]> findJoinedCountPerYear();

    /** Returns [year, resignedCount] grouped by resignationDate year, ordered ascending */
    @Query("SELECT YEAR(m.resignationDate), COUNT(m) FROM Member m WHERE m.resignationDate IS NOT NULL GROUP BY YEAR(m.resignationDate) ORDER BY YEAR(m.resignationDate) ASC")
    List<Object[]> findResignedCountPerYear();

    List<Member> findBySalutationIgnoreCase(String sozialkontoSalutation);

    List<Member> findByFirstNameAndLastName(String adminAccountFirstName, String adminAccountLastName);



}