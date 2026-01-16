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

    List<TimeCheque> findAllByAssignedToIdOrderByOrderDateDesc(Long assignedTo);

    List<TimeCheque> findAllByAccountingEntryIsNullOrderByOrderDateAsc();

    TimeCheque findTopByAssignedToIdOrderByOrderDateDesc(Long assignedTo);

    @Query("SELECT t FROM TimeCheque t WHERE (t.accountingEntry IS NULL AND t.amount > 0) ORDER BY t.orderDate ASC")
    List<TimeCheque> findAllNotAccountedTimeCheques();
}


/*
List<TimeCheque> findAllByAccountingEntryIsNullOrderByOrderDateAsc():

SELECT t FROM TimeCheque t WHERE t.accountingEntry IS NULL ORDER BY t.orderDate ASC
*/

