/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.accountings;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

public interface AccountingRepository extends ListCrudRepository<AccountingEntry, Long> {

    List<AccountingEntry> findAll();

    /** All entries filtered by year (via accountingDate) and transactionType, ordered by accountingDate desc. */
    @Query("SELECT a FROM AccountingEntry a WHERE YEAR(a.accountingDate) = :year AND a.transactionType = :transactionType ORDER BY a.accountingDate DESC")
    List<AccountingEntry> findByYearAndTransactionType(@Param("year") int year, @Param("transactionType") String transactionType);

    /** Filtered additionally by accountableClass. */
    @Query("SELECT a FROM AccountingEntry a WHERE YEAR(a.accountingDate) = :year AND a.transactionType = :transactionType AND a.accountableClass = :accountableClass ORDER BY a.accountingDate DESC")
    List<AccountingEntry> findByYearAndTransactionTypeAndAccountableClass(@Param("year") int year, @Param("transactionType") String transactionType, @Param("accountableClass") String accountableClass);

    /** All distinct accountableClass values (for the filter drop-down). */
    @Query("SELECT DISTINCT a.accountableClass FROM AccountingEntry a WHERE a.accountableClass IS NOT NULL ORDER BY a.accountableClass")
    List<String> findDistinctAccountableClasses();
}
