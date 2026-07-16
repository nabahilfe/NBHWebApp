package eu.nabahilfe.webapp.osm;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NominatimResult(String lat, String lon, @JsonProperty("display_name") String displayName)
{
    public Double getLatitude() {
        return Double.parseDouble(lat);
    }

    public Double getLongitude() {
        return Double.parseDouble(lon);
    }
}
