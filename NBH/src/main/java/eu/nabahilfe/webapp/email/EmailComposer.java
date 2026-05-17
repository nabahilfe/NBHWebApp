/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.email;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.nabahilfe.webapp.NumberFormatter;



@Component
public class EmailComposer {
	private static final String eMailFooter = 
            "<p>Liebe Grüße,<br>Dein Nachbarschaftshilfe Team.</p>"
            + "<p><small>Diese E-Mail wurde automatisch generiert, bitte nicht antworten!</small></p>";

    private final String baseUrl;

    public EmailComposer(@Value("${app.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }


    public EmailDetails composeTimeChecksToBookEmail(String recipient, String emailSalutation, int timeChecksToBook) {
        EmailDetails details = new EmailDetails(
	            recipient,
	            "Es gibt neue Zeitchecks zu verbuchen!",
	            "<strong>" + emailSalutation + "</strong><br>"
                + "<p>"
                + "Es gibt <strong>" + timeChecksToBook + "</strong> Zeitchecks zu verbuchen!<br>"
                + "<a href=\"" + baseUrl + "/timecheques/unaccounted\">Link zu den zu verbuchenden Zeitchecks</a>"
                + "</p>"
                + eMailFooter
        );
        return details;
    }


    public EmailDetails composeConfirmationCodeEmail(String recipient, String name, String code) {
        EmailDetails details = new EmailDetails(
                recipient,
                "Registrierungscode für die Nachbarschaftshilfe",
                "<p><strong>Hallo " + name + "!</strong></p>"
                + "Vielen Dank für Deine Registrierung bei der Nachbarschaftshilfe!<br>"
                + "Hier ist dein Registrierungscode:<br>"
                + "<h3>&nbsp;&nbsp;&nbsp;" + code + "</h3>"
                + "<p>Bitte gib diesen Code auf der Registrierungsseite ein, um deine Registrierung abzuschließen.</p>"
                + "<p>Falls du diese E-Mail nicht angefordert hast, kannst du sie einfach ignorieren.</p>"
                + eMailFooter
        );
        return details;
    }


    public EmailDetails composeTimeChecksBought(String recipient, String emailSalutation, int amount, double totalPrice) {
    	String subject = totalPrice == 0 ? "Zeitchecks erhalten" : "Zeitchecks gekauft";
    	String message = totalPrice == 0 ? "Du hast für deine Mitgliedschaft in der Nachbarschaftshilfe <strong>" + amount + "</strong> Zeitchecks erhalten!" 
    			: "Du hast <strong>" + amount + "</strong> Zeitchecks zum Preis von <strong>€ " + NumberFormatter.numberDE(totalPrice) + "</strong> bestellt und erhalten!";
    	
        EmailDetails details = new EmailDetails(
	            recipient,
	            subject,
	            "<p><strong>" + emailSalutation + "</strong></p>"
	    		+ "<p>" + message + "</p>"
	            + eMailFooter
        );
        return details;
    }
    

}
