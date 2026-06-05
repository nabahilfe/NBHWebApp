/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.domaintypes;

public enum AmountDomainType {

    // Never Ever Change this names! They are used in the database!

    MEMBERSHIP_FEE("Mitgliedsbeitrag"),    			// Mitgliedsbeitrag
    TIMECHEQUE_FEE("Zeitscheck Kosten");     		// Zeitscheck Kosten

    private final String description;

    AmountDomainType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}