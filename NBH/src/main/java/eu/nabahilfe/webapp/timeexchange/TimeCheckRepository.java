package eu.nabahilfe.webapp.timeexchange;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;

import eu.nabahilfe.webapp.members.Member;


public interface TimeCheckRepository extends ListCrudRepository<TimeCheque, Long> {


    Page<TimeCheque> findAll(Pageable pageable);

    Page<TimeCheque> findAllByAssignedTo(Pageable pageable, Member member);

    Integer countByAssignedTo(Member member);

}
