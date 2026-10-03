package eu.nabahilfe.webapp.charts;

/**
 * Repräsentiert die Anzahl der Personen eines Jahrzehnts aufgeteilt nach Geschlecht.
 * Wichtig: Die Liste im Controller muss von oben nach unten (90+ bis 0-9) befüllt sein.
 */
public record AgeDecadeData(int male, int female) {}
