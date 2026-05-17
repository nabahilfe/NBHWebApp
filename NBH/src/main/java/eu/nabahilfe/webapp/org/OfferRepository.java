/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.org;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

public interface OfferRepository extends ListCrudRepository<Offer, Long> {

    Optional<Offer> findById(Long id);

    Offer findByCode(String code);

    List<Offer> findAllByOrderByCodeAsc();
}