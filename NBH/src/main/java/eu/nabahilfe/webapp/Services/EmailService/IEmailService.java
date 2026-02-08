package eu.nabahilfe.webapp.Services.EmailService;

public interface IEmailService {
	String sendEmail(EmailDetails details);
	String sendEmailHtml(EmailDetails details);
	String sendEmailWithAttachement(EmailDetails details);
}