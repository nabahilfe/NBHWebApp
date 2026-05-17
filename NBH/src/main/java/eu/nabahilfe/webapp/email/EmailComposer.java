/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class EmailComposer {

    private final String baseUrl;

    public EmailComposer(@Value("${app.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public EmailDetails composeWelcomeEmail(String recipient, String name) {
        EmailDetails details = new EmailDetails(
            recipient,
            "Willkommen bei der Nachbarschaftshilfe!",
            "<h3>Willkommen " + name + "!<h3>"
                + "<p>"
                + "<a href=\"https://nabahilfe.eu\"> zur Website</a>"
                + "</p>"
        );

        return details;
    }


    public EmailDetails composeTimeChecksToBookEmail(String recipient, String name, int timeChecksToBook) {
        EmailDetails details = new EmailDetails(
            recipient,
            "Es gibt neue Zeitchecks zu verbuchen!",
            "<h3>Hallo " + name + "</h3>"
                    + "<p>"
                    + "Es gibt <strong>" + timeChecksToBook + "</strong> Zeitchecks zu verbuchen!<br>"
                    + "<a href=\"" + baseUrl + "/timecheques/unaccounted\">Link zu den zu verbuchenden Zeitchecks</a>"
                    + "</p>"
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
                + "<p>Liebe Grüße,<br>Dein Nachbarschaftshilfe Team</p>"
                + "<br>"
                + "<p><i>Diese E-Mail wurde automatisch generiert, bitte nicht antworten!</i></p>"
                );
        return details;
    }


}
