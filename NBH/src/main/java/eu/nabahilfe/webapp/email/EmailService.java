/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.email;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;
    

    @Value("${spring.mail.username}") private String sender;


    @Override
    public String sendEmail(EmailDetails details) {
        System.out.println("sender: " + sender);

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
            String html =
                """
                <html>
                    <body>"""
                        + details.getBody()
                        + """
                    </body>
                </html>
                """;

            helper.setFrom(sender);
            helper.setTo(details.getRecipient());
            helper.setSubject(details.getSubject());
            helper.setText(html, true);

            if (details.getAttachment() != null && !details.getAttachment().isEmpty()) {
                FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
                helper.addAttachment(file.getFilename(), file);
            }

            javaMailSender.send(mimeMessage);

            return "Mail sent successfully";
        }
        catch  (Exception e) {
            log.error("Error while sending Mail: " + e);
            return "Error while sending Mail: " + e;
        }
    }

}