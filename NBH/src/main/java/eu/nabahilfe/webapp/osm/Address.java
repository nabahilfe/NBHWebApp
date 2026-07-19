package eu.nabahilfe.webapp.osm;

public record Address(
        String street,
        String houseNumber,
        String postalCode,
        String city,
        String country)
{
}