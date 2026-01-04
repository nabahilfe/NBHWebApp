package eu.nabahilfe.webapp.timeexchange;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;

import eu.nabahilfe.webapp.members.Member;


public interface TimeCheckRepository extends ListCrudRepository<TimeCheque, Long> {


    Page<TimeCheque> findAll(Pageable pageable);

    Page<TimeCheque> findAllByAssignedTo(Pageable pageable, Member member);

    Integer countByAssignedTo(Member member);

    List<TimeCheque> findLast10ByAssignedToIdOrderByOrderDateDesc(Long assignedTo);

}
