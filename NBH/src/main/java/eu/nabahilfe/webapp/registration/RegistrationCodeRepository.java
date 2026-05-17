/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.registration;

import org.springframework.data.repository.ListCrudRepository;

public interface RegistrationCodeRepository extends ListCrudRepository<RegistrationCode, Long> {

    RegistrationCode findFirstByEmailOrderByIdDesc(String email);

    long deleteByEmail(String email);

}
