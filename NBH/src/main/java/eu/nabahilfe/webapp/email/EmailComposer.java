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
				"Bestätigungs Code",
				"<h3>" + code + "</h3>"
				);
		return details;
	}

	
}
