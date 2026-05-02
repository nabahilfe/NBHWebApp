package eu.nabahilfe.webapp.domaintypes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface AmountDomainValueRepository extends ListCrudRepository<AmountDomainValue, Long> {

    Optional<AmountDomainValue> findById(Long id);

    List<AmountDomainValue> findByCode(String code);

    List<AmountDomainValue> findAllByOrderByCodeAsc();

    /** Find the currently open record (validTo = 9999-12-31) for a given code */
    @Query("SELECT a FROM AmountDomainValue a WHERE a.code = :code AND a.validTo = :openDate")
    Optional<AmountDomainValue> findOpenByCode(String code, LocalDate openDate);
}
