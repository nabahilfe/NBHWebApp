/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.accountings;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends ListCrudRepository<Transaction, Long> {

    /** All unaccounted transactions (accountedBy is null) of a given type, ordered by transactionDate desc. */
    @Query("SELECT t FROM Transaction t WHERE t.accountedBy IS NULL AND t.transactionType = :transactionType ORDER BY t.transactionDate DESC")
    List<Transaction> findUnaccountedByType(@Param("transactionType") String transactionType);

    /** All transactions of a given type, ordered by transactionDate desc. */
    @Query("SELECT t FROM Transaction t WHERE t.transactionType = :transactionType ORDER BY t.transactionDate DESC")
    List<Transaction> findByTransactionType(@Param("transactionType") String transactionType);

}
