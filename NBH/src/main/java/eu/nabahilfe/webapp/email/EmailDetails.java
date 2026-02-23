package eu.nabahilfe.webapp.email;


public class EmailDetails {
	private String recipient;
	private String subject;
	private String body;
	private String attachment;
	
	public EmailDetails(String recipient, String subject, String message, String attachment) {
		this.recipient = recipient;
		this.subject = subject;
		this.body = message;
		this.attachment = attachment;
	}
	public EmailDetails(String recipient, String subject, String message) {
		this.recipient = recipient;
		this.subject = subject;
		this.body = message;
	}
	
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String message) {
		this.body = message;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	

	
}