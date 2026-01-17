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

}


/*
List<TimeCheque> findAllByAccountedByIsNullOrderByOrderDateAsc():

SELECT t FROM TimeCheque t WHERE t.accountedBy IS NULL ORDER BY t.orderDate ASC
*/

