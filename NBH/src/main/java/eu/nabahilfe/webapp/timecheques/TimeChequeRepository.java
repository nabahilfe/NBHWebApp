package eu.nabahilfe.webapp.timecheques;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import eu.nabahilfe.webapp.members.Member;


public interface TimeChequeRepository extends ListCrudRepository<TimeCheque, Long> {

    Page<TimeCheque> findAll(Pageable pageable);

    Page<TimeCheque> findAllByAssignedTo(Pageable pageable, Member member);

    Integer countByAssignedTo(Member member);

    List<TimeCheque> findAllByAssignedTo_IdOrderByTransactionDateDesc(Long assignedTo);

    List<TimeCheque> findAllByAccountedBy_IdIsNullAndAmountGreaterThanOrderByTransactionDateAsc(Double amount);

    TimeCheque findTopByAssignedTo_IdOrderByTransactionDateDesc(Long assignedTo);

    /** Returns [memberFullName, totalHours, chequeCount] per member for all time cheques in the given year, sorted by total hours desc */
    @Query("""
            SELECT CONCAT(m.firstName, ' ', m.lastName), SUM(t.hours), COUNT(t)
            FROM TimeCheque t JOIN t.assignedTo m
            WHERE YEAR(t.transactionDate) = :year
            GROUP BY m.id, m.firstName, m.lastName
            ORDER BY SUM(t.hours) DESC
            """)
    List<Object[]> findStatsByYear(int year);
}