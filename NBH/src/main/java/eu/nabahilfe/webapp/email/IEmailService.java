package eu.nabahilfe.webapp.email;

public interface IEmailService {
	String sendEmail(EmailDetails details);
	String sendEmailHtml(EmailDetails details);
	String sendEmailWithAttachement(EmailDetails details);
}