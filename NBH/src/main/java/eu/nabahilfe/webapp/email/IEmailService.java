/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.email;

public interface IEmailService {
	String sendEmailPlainText(EmailDetails details);
	String sendEmailHtml(EmailDetails details);
	String sendEmailWithAttachement(EmailDetails details);
}