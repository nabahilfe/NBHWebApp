package eu.nabahilfe.webapp.accountings;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

public interface AccountingRepository extends ListCrudRepository<AccountingEntry, Long> {


    List<AccountingEntry> findAllByAccountableTableNameContaining(String name);


}
