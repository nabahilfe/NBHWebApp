/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.accountings;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

public interface AccountingRepository extends ListCrudRepository<AccountingEntry, Long> {


    List<AccountingEntry> findAll();


}
