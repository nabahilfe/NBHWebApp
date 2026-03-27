package eu.nabahilfe.webapp.timetransfers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

public interface TimeTransferRepository extends ListCrudRepository<TimeTransfer, Long> {

    List<TimeTransfer> findAllByOrderByDateOfServiceDesc();

    List<TimeTransfer> findTop10ByFromMember_IdOrToMember_IdOrderByDateOfServiceDesc(Long fromMemberId, Long toMemberId);


    List<TimeTransfer> findAllByToMember_IdOrderByDateOfServiceDesc(Long memberId);


    List<TimeTransfer> findAllByFromMember_IdOrderByDateOfServiceDesc(Long memberId);

    Optional<TimeTransfer> findByIdAndFromMember_Id(Long id, Long fromMemberId);

}


