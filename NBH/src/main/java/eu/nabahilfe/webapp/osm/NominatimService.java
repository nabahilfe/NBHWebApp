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


    /**
     * Validates and geocodes the member's address using the OSM Nominatim API.
     * On success, sets latitude/longitude on the member and returns {@code null}.
     * On failure (no result, or the found result does not sufficiently match the
     * input address), latitude/longitude are cleared and the display name of the
     * (best-effort, non-matching) location found by Nominatim is returned, so the
     * caller can show it to the user for feedback. Returns {@code null} if no
     * result at all could be found for the given address.
     */
    public String validateAndUpdateMemberAddress(Member m) {

        // TODO: Country should be a field in the Member entity, not hardcoded to "Österreich"
        Address address = new Address(m.getStreet(), m.getNumber(), m.getZip(), m.getCity(), "Österreich");
        Optional<NominatimResult> result = localizeAddress(address);

        if (result.isPresent()) {
            NominatimResult loc = result.get();

            // we need to check if we have the real address - displayName must match number and street, zip and city must be contained
            // otherwise we have a wrong address (e.g. if the number is not found, it will return the city center)

            String displayNameUPPER = loc.displayName().toUpperCase();
            List<String> displayNameParts = List.of(displayNameUPPER.split(","));

            // Check if the displayNameParts contains the number, street, zip, and city
            // Number must be an exact match of the first or second part of the displayNameParts

            boolean matchesNumber = displayNameParts.stream().anyMatch(part -> part.trim().equalsIgnoreCase(m.getNumber()));
            boolean matchesStreet = displayNameParts.stream().anyMatch(part -> part.trim().equalsIgnoreCase(m.getStreet().toUpperCase()));
            boolean matchesZip = displayNameParts.stream().anyMatch(part -> part.trim().contains(m.getZip().toUpperCase()));
            boolean matchesCity = displayNameParts.stream().anyMatch(part -> part.trim().contains(m.getCity().toUpperCase()));

            if (matchesNumber && matchesStreet && matchesZip && matchesCity) {
                // log.info("Address validated and geocoded: {} -> lat: {}, lon: {}", address, loc.getLatitude(), loc.getLongitude());
                m.setLatitude(loc.getLatitude());
                m.setLongitude(loc.getLongitude());
                return null;
            }
            else {
                log.info("--------------- Address validation does not match --------------------");
                log.info("DisplayName: {}", loc.displayName());
                log.warn("Address could not be validated: {} -> geocoded to {}, which does not match the input address", address, loc.displayName());
                log.info("number match: {}", matchesNumber);
                log.info("street match: {}", matchesStreet);
                log.info("zip match:    {}", matchesZip);
                log.info("city match:   {}", matchesCity);

                m.setLatitude(null);
                m.setLongitude(null);
                return loc.displayName();
            }
        }
        else {
            log.info("--------------- Address validation failed ---------------------");
            log.warn("Address could not be validated or geocoded at all: {}", address);
            m.setLatitude(null);
            m.setLongitude(null);
            return "Diese Adresse konnte nicht gefunden werden, Adresse prüfen und ggf. korrigieren.";
        }

    }


}