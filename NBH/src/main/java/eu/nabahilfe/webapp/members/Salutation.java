package eu.nabahilfe.webapp.members;

// see https://thorben-janssen.com/hibernate-enum-mappings/
// we use the following mapping
// @Enumerated(EnumType.STRING)
// to store the enum value as a string in the database

public enum Salutation {
    //do not change the names, they are used in the database!
    Herr,
    Frau,
    SOZIALKONTO     // Es darf nur ein "Mitglied" mit Sozialkonto geben
    // für Diverse wird kein Eintrag verwendet, Felb bleibt dann leer
}
