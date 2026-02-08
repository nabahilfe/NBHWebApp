package eu.nabahilfe.webapp.Services.EmailService;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService implements IEmailService {
	
	@Autowired 
	JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}") private String sender;

	@Override
	public String sendEmail(EmailDetails details) {
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			
			mailMessage.setFrom(sender);
			mailMessage.setTo(details.getRecipient());
			mailMessage.setSubject(details.getSubject());
			mailMessage.setText(details.getBody());
			
			javaMailSender.send(mailMessage);
			return "Mail sent successfully";
		}
		
		catch (Exception e) {
			return "Error while sending Mail: " + e;
		}
	}
	
	@Override
	public String sendEmailHtml(EmailDetails details) {
		return sendEmailWithAttachement(details);
	}

	@Override
	public String sendEmailWithAttachement(EmailDetails details) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			
			helper.setFrom(sender);
			helper.setTo(details.getRecipient());
			helper.setSubject(details.getSubject());
			helper.setText(details.getBody());
			
			if(details.getAttachment() != null && !details.getAttachment().isEmpty()) {
				FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
				helper.addAttachment(file.getFilename(), file);
			}
			
			javaMailSender.send(mimeMessage);
			
			return "Mail sent successfully";
		}
		catch  (Exception e) {
			return "Error while sending Mail: " + e;
		}
	}


	
}