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
            "<p><small>Diese E-Mail wurde automatisch generiert, bitte nicht antworten!</small></p>";

    private final String baseUrl;

    public EmailComposer(@Value("${app.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }


    public EmailDetails composeTimeChecksToBookEmail(String recipient, String emailSalutation, int timeChecksToBook) {
        EmailDetails details = new EmailDetails(
                recipient,
                "Es gibt neue Zeitschecks zu verbuchen!",
                "<strong>" + emailSalutation + "</strong><br>"
                + "<p>"
                + "Es gibt <strong>" + timeChecksToBook + "</strong> Zeitschecks zu verbuchen!<br>"
                + "<a href=\"" + baseUrl + "/timecheques/unaccounted\">Link zu den zu verbuchenden Zeitschecks</a>"
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


    public EmailDetails composeTimeChecksBought(
            String emailRecipient, String salutationRecipient,
            String emailCreatedBy, String nameCreatedBy,
            int amount, double totalPrice) {

        // we need some business logic here to determine the subject and message based on
        // whether the user bought time checks or received them for free or by whome the TC was created

        String subject = totalPrice == 0 ? "Zeitschecks erhalten" : "Zeitschecks gekauft";
        String message = null;

        if (totalPrice == 0)
            message = "Du hast für deine Mitgliedschaft in der Nachbarschaftshilfe <strong>" + amount + "</strong> Zeitschecks erhalten!";

        else if (emailCreatedBy.equals(emailRecipient))
            message = "Du hast <strong>" + amount + "</strong> Zeitschecks zum Preis von <strong>€ " + NumberFormatter.numberDE(totalPrice) + "</strong> selbst bestellt und erhalten!";

        else
            message = "Du hast <strong>" + amount + "</strong> Zeitschecks zum Preis von <strong>€ " + NumberFormatter.numberDE(totalPrice) + "</strong> erhalten!" + "<br>"
                    + "Diese wurden in deinem Auftrag von <strong>" + nameCreatedBy + "</strong> bestellt.";

        EmailDetails details = new EmailDetails(
                emailRecipient,
                subject,
                "<p><strong>" + salutationRecipient + "</strong></p>"
                + "<p>" + message + "</p>"
                + eMailFooter
        );
        return details;
    }


    // email for the recipient of a time cheque transfer (the one who receives the time cheque)
    public EmailDetails composeTimeChequeTransferToEmail(
            String emailToMember, String salutationToMember,
            String nameFromMember,
            int hours, String offerTitle, String note) {

        String subject = "Zeitscheck von " + nameFromMember + " erhalten";

        String message = "Du hast einen Zeitscheck von <strong>" + nameFromMember + "</strong> erhalten!<br>"
                + "<br>Anzahl der Stunden: <strong>" + hours + "</strong><br>"
                + "Art der Leistung: <strong>" + offerTitle + "</strong><br>"
                + (note != null && !note.isBlank() ? "Anmerkung: <em>" + note + "</em><br>" : "");

        EmailDetails details = new EmailDetails(
                emailToMember,
                subject,
                "<p><strong>" + salutationToMember + "</strong></p>"
                + "<p>" + message + "</p>"
                + eMailFooter
        );
        return details;
    }


    public EmailDetails composeTimeChequeTransferFromEmail(
            String emailFromMember, String salutationFromMember,
            String nameToMember, String nameCreatedBy,
            int hours, String offerTitle, String note) {

        String subject = "Zeitscheck an " + nameToMember + " übergeben";
        String message = "Von <strong>" + nameCreatedBy + "</strong> wurde in deinem Auftrag ein Zeitscheck an <strong>" + nameToMember + "</strong> übergeben!<br>"
                + "<br>Anzahl der Stunden: <strong>" + hours + "</strong><br>"
                + "Art der Leistung: <strong>" + offerTitle + "</strong><br>"
                + (note != null && !note.isBlank() ? "Anmerkung: <em>" + note + "</em><br>" : "");

        EmailDetails details = new EmailDetails(
                emailFromMember,
                subject,
                "<p><strong>" + salutationFromMember + "</strong></p>"
                + "<p>" + message + "</p>"
                + eMailFooter
        );
        return details;
    }

}
