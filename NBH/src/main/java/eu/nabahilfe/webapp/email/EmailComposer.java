package eu.nabahilfe.webapp.email;

public class EmailComposer {

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

    public EmailDetails composeTimeChecksToBookEmail(String recipient, String name) {
        EmailDetails details = new EmailDetails(
            recipient,
            "neue Zeitchecks zu verbuchen",
            "<h3>Hallo " + name + "</h3>"
                    + "<p>"
                    + "<a href=\"https://nabahilfe.eu\"> zur Website</a>"
                    + "</p>"
        );
        return details;
    }

    public EmailDetails composeConfirmationCodeEmail(String recipient, String name, String code) {
        EmailDetails details = new EmailDetails(
                recipient,
                "Registrierungscode für die Nachbarschaftshilfe",
                "<p>Hallo<strong> " + name + "</strong>!</p>"
                + "Vielen Dank für dein Registrierung bei der Nachbarschaftshilfe!<br>"
                + "Hier ist dein Registrierungscode:<br>"
                + "<h3>&nbsp;&nbsp;&nbsp;" + code + "</h3>"
                + "<p>Bitte gib diesen Code auf der Registrierungsseite ein, um deine Registrierung abzuschließen.</p>"
                + "<p>Falls du diese E-Mail nicht angefordert hast, kannst du sie einfach ignorieren.</p>"
                + "<p>Liebe Grüße,<br>Dein Nachbarschaftshilfe Team</p>"
                );
        return details;
    }


}
