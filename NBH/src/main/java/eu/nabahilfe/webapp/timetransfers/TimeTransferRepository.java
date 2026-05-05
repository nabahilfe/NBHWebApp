package eu.nabahilfe.webapp.timetransfers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface TimeTransferRepository extends ListCrudRepository<TimeTransfer, Long> {

    List<TimeTransfer> findAllByOrderByDateOfServiceDesc();

    List<TimeTransfer> findTop10ByFromMember_IdOrToMember_IdOrderByDateOfServiceDesc(Long fromMemberId, Long toMemberId);


    List<TimeTransfer> findAllByToMember_IdOrderByDateOfServiceDesc(Long memberId);


    List<TimeTransfer> findAllByFromMember_IdOrderByDateOfServiceDesc(Long memberId);

    Optional<TimeTransfer> findByIdAndFromMember_Id(Long id, Long fromMemberId);

    /** Returns [memberFullName, totalHours, transferCount] for members who received hours in the given year, sorted by total hours desc */
    @Query("""
            SELECT CONCAT(m.firstName, ' ', m.lastName), SUM(t.hours), COUNT(t)
            FROM TimeTransfer t JOIN t.toMember m
            WHERE YEAR(t.dateOfService) = :year
            GROUP BY m.id, m.firstName, m.lastName
            ORDER BY SUM(t.hours) DESC
            """)
    List<Object[]> findEarnedHoursStatsByYear(int year);

    /** Returns [memberFullName, totalHours, transferCount] for members who gave hours in the given year, sorted by total hours desc */
    @Query("""
            SELECT CONCAT(m.firstName, ' ', m.lastName), SUM(t.hours), COUNT(t)
            FROM TimeTransfer t JOIN t.fromMember m
            WHERE YEAR(t.dateOfService) = :year
            GROUP BY m.id, m.firstName, m.lastName
            ORDER BY SUM(t.hours) DESC
            """)
    List<Object[]> findGivenHoursStatsByYear(int year);

}


