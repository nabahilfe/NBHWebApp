/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NbhApplication {

    private static final Logger log = LoggerFactory.getLogger(NbhApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(NbhApplication.class, args);

        log.info("\nNBH Application wurde gestartet!");
    }

}
