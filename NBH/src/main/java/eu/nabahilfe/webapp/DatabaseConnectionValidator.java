/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

@Configuration  public class DatabaseConnectionValidator {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionValidator.class);

    //    @Profile("dev")
    @Bean
    CommandLineRunner validateConnection(DataSource dataSource) {
        return _ -> {
            try (Connection conn = dataSource.getConnection()) {
                DatabaseMetaData metaData = conn.getMetaData();
                log.info("--> SUCCESS: Connected to {} version {}", metaData.getDatabaseProductName(),
                        metaData.getDatabaseProductVersion());
            } catch (Exception e) {
                log.error("--> FAILURE: Could not connect to database. Check Docker container.", e);
                // Fail fast allows us to fix config immediately
                throw new IllegalStateException("Database connection failed", e);
            }
        };
    }
}
