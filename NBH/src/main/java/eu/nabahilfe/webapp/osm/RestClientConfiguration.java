package eu.nabahilfe.webapp.osm;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    RestClient nominatimRestClient() {
        return RestClient.builder()
                .baseUrl("https://nominatim.openstreetmap.org")
                .defaultHeader(HttpHeaders.USER_AGENT,
                        "Nachbarschaftshilfe/1.0 (sysadmin.ma@nabahilfe.eu;+https://nabahilfe.eu)")
                .build();
    }
}