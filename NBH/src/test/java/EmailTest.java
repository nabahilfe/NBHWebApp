
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import eu.nabahilfe.webapp.NbhApplication;
import eu.nabahilfe.webapp.Services.EmailService.EmailDetails;
import eu.nabahilfe.webapp.Services.EmailService.EmailService;

@SpringBootTest(classes = NbhApplication.class)
public class EmailTest {
	@Autowired EmailService emailService;
	String recipient = "test@nabahilfe.eu";

	@Value("${spring.mail.username}") String sender;

	
	@Test
	void sendEmailTest() {
		String id = UUID.randomUUID().toString();
		EmailDetails emailDetails = new EmailDetails(recipient, id, "test email");
		System.out.println(emailService.sendEmail(emailDetails));
	}
}
