package eu.nabahilfe.webapp.osm;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import eu.nabahilfe.webapp.members.Member;

@Service
public class NominatimService {

    private final RestClient restClient;

    private static final Logger log = LoggerFactory.getLogger(NominatimService.class);


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


    public void validateAndUpdateMemberAddress(Member m) {

        // TODO: Country should be a field in the Member entity, not hardcoded to "Österreich"
        Address address = new Address(m.getStreet(), m.getNumber(), m.getZip(), m.getCity(), "Österreich");
        Optional<NominatimResult> result = localizeAddress(address);

        if (result.isPresent()) {
            NominatimResult loc = result.get();

            // we need to check if we have the real address - displaName must start with number and street, zip and city must be contained
            // otherwise we have a wrong address (e.g. if the number is not found, it will return the city center)

            String displayNameUPPER = loc.displayName().toUpperCase();
            if ((displayNameUPPER.startsWith(m.getNumber().toUpperCase()) || displayNameUPPER.contains(m.getNumber() + ",".toUpperCase())) &&
                displayNameUPPER.contains(m.getStreet().toUpperCase()) &&
                displayNameUPPER.contains(m.getZip().toUpperCase()) && displayNameUPPER.contains(m.getCity().toUpperCase())) {

                log.info("Address validated and geocoded: {} -> lat: {}, lon: {}", address, loc.getLatitude(), loc.getLongitude());
                m.setLatitude(loc.getLatitude());
                m.setLongitude(loc.getLongitude());
            }
            else {
                log.warn("Address could not be validated: {} -> geocoded to {}, which does not match the input address", address, loc.displayName());
                m.setLatitude(null);
                m.setLongitude(null);
            }
        }
        else {
            log.warn("Address could not be validated or geocoded: {}", address);
            m.setLatitude(null);
            m.setLongitude(null);
        }
    }


}