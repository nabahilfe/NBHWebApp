

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;


import eu.nabahilfe.webapp.NbhApplication;
import eu.nabahilfe.webapp.email.EmailDetails;
import eu.nabahilfe.webapp.email.EmailService;


@SpringBootTest(classes = NbhApplication.class)
public class EmailTest {
	@Autowired private EmailService emailService;
	@Value("${spring.mail.username}") private String sender;

	private String recipient = "test@nabahilfe.eu";

	
	@Test
	void sendEmailTest() {

		String id = UUID.randomUUID().toString();
		EmailDetails emailDetails = new EmailDetails(recipient, id, "test email");
		System.out.println(emailService.sendEmailPlainText(emailDetails));
	}
	
	@Test
	void sendEmailHtmlTest() {

		String id = UUID.randomUUID().toString();
		String htmlBody = "<h2>test email</h2><p>this is a html body</p>";
		EmailDetails emailDetails = new EmailDetails(recipient, id, htmlBody);
		System.out.println(emailService.sendEmailHtml(emailDetails));
	}
	
	@Test
	void sendTimeChecksToBookEmailTest() {
		//emailService.sendTimeChecksToBookEmail();
	}
	
	
	
	
	
}
