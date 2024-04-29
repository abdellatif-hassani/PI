package com.example.gmailapi;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.mail.internet.InternetAddress;
@Service
public class EmailService {
    private Gmail gmailService;
    public EmailService(Gmail gmailService) {
        this.gmailService = gmailService;
    }



    public void sendEmail(String recipientEmail, String subject, String body) throws IOException, MessagingException, javax.mail.MessagingException {
        Message message = createMessageWithEmail(createEmail(recipientEmail, subject, body));
        gmailService.users().messages().send("me", message).execute();
    }

    private Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException, javax.mail.MessagingException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    private MimeMessage createEmail(String to, String subject, String bodyText) throws MessagingException, javax.mail.MessagingException {
        Properties props = new Properties();
        Session session =Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("your_email@example.com"));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }
}
