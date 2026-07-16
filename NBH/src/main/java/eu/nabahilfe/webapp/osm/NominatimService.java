package eu.nabahilfe.webapp.osm;

import java.util.List;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NominatimService {

    private final RestClient restClient;

    public NominatimService(RestClient nominatimRestClient) {
        this.restClient = nominatimRestClient;
    }

    public Optional<NominatimResult> localizeAddress(Address address) {

        List<NominatimResult> result = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("street",
                                address.houseNumber() + " " + address.street())
                        .queryParam("city", address.city())
                        .queryParam("postalcode", address.postalCode())
                        .queryParam("country", address.country())
                        .queryParam("format", "jsonv2")
                        .queryParam("limit", "1")
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<NominatimResult>>() {});

        if (result == null || result.isEmpty()) {
            return Optional.empty();
        }

        var location = result.getFirst();
        return Optional.of(location);
    }
}