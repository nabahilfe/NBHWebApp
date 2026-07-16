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
    @Query("SELECT a FROM AccountingEntry a LEFT JOIN FETCH a.createdBy WHERE YEAR(a.accountingDate) = :year AND a.transactionType = :transactionType ORDER BY a.accountingDate DESC")
    List<AccountingEntry> findByYearAndTransactionType(@Param("year") int year, @Param("transactionType") String transactionType);

    /** Filtered by year, month and transactionType. */
    @Query("SELECT a FROM AccountingEntry a LEFT JOIN FETCH a.createdBy WHERE YEAR(a.accountingDate) = :year AND MONTH(a.accountingDate) = :month AND a.transactionType = :transactionType ORDER BY a.accountingDate DESC")
    List<AccountingEntry> findByYearAndMonthAndTransactionType(@Param("year") int year, @Param("month") int month, @Param("transactionType") String transactionType);

    /** Filtered additionally by accountableName. */
    @Query("SELECT a FROM AccountingEntry a LEFT JOIN FETCH a.createdBy WHERE YEAR(a.accountingDate) = :year AND a.transactionType = :transactionType AND a.accountableName = :accountableName ORDER BY a.accountingDate DESC")
    List<AccountingEntry> findByYearAndTransactionTypeAndAccountableClass(@Param("year") int year, @Param("transactionType") String transactionType, @Param("accountableName") String accountableName);

    /** Filtered by year, month, transactionType and accountableName. */
    @Query("SELECT a FROM AccountingEntry a LEFT JOIN FETCH a.createdBy WHERE YEAR(a.accountingDate) = :year AND MONTH(a.accountingDate) = :month AND a.transactionType = :transactionType AND a.accountableName = :accountableName ORDER BY a.accountingDate DESC")
    List<AccountingEntry> findByYearAndMonthAndTransactionTypeAndAccountableClass(@Param("year") int year, @Param("month") int month, @Param("transactionType") String transactionType, @Param("accountableName") String accountableName);

    /** All distinct accountableName values (for the filter drop-down). */
    @Query("SELECT DISTINCT a.accountableName FROM AccountingEntry a WHERE a.accountableName IS NOT NULL ORDER BY a.accountableName")
    List<String> findDistinctAccountableClasses();
}
