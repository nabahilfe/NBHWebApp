package eu.nabahilfe.webapp.domaintypes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface AmountDomainValueRepository extends ListCrudRepository<AmountDomainValue, Long> {

    Optional<AmountDomainValue> findById(Long id);

    List<AmountDomainValue> findByCode(String code);

    List<AmountDomainValue> findAllByOrderByValidFromDesc();

    List<AmountDomainValue> findByCodeOrderByValidFromDesc(String code);

    /** Find the record with the highest validFrom for a given code */
    @Query("SELECT a FROM AmountDomainValue a WHERE a.code = :code ORDER BY a.validFrom DESC LIMIT 1")
    Optional<AmountDomainValue> findLatestByCode(String code);

    /** Find the record for a given code whose validTo equals the given date (used to re-open the predecessor after deletion) */
    @Query("SELECT a FROM AmountDomainValue a WHERE a.code = :code AND a.validTo = :validTo")
    Optional<AmountDomainValue> findByCodeAndValidTo(String code, LocalDate validTo);

    @Query("SELECT a FROM AmountDomainValue a WHERE a.code = :code AND a.validTo = :openDate")
    Optional<AmountDomainValue> findOpenByCode(String code, LocalDate openDate);
}
