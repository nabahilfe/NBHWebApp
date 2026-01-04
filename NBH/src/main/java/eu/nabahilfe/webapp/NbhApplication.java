package eu.nabahilfe.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NbhApplication {

    private static final Logger log = LoggerFactory.getLogger(NbhApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(NbhApplication.class, args);

        log.info("\nNBH Application wurde gestartet!");
    }

}
