package eu.nabahilfe.webapp.timeexchange;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

public interface TimeTransferRepository extends ListCrudRepository<TimeTransfer, Long> {

    List<TimeTransfer> findLast10ByFromMemberIdOrToMemberIdOrderByIdDesc(Long fromMemberId, Long toMemberId);

}


