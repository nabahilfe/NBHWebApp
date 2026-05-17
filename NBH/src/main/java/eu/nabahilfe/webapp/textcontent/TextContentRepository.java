/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.textcontent;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextContentRepository extends JpaRepository<TextContent, Long> {

    // Find by element code (exact)
    Optional<TextContent> findByContentCode(String elementCode);


}
