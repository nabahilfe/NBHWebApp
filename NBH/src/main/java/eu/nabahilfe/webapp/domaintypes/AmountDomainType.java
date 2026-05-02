package eu.nabahilfe.webapp.domaintypes;

public enum AmountDomainType {

    // Never Ever Change this names! They are used in the database!

    MEMBER_FEE("Mitgliedsbeitrag"),    			// Mitgliedsbeitrag
    TIMECHEQUE_FEE("Zeitscheck Kosten");     	// Zeitscheck Kosten

    private final String description;

    AmountDomainType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}