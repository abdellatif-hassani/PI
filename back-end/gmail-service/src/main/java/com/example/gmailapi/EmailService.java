package com.example.gmailapi;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;
import entiites.Attachment;

import javax.activation.DataHandler;
import javax.mail.Multipart;
import javax.mail.internet.*;
import javax.mail.Session;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import javax.mail.internet.InternetAddress;
import javax.mail.util.ByteArrayDataSource;

import static com.google.api.client.json.gson.GsonFactory.getDefaultInstance;

@Service
@Data
public class EmailService {
    private Gmail gmailService;

    public void setGmailService(Gmail gmailService) {
        this.gmailService = gmailService;
    }

    public Message sendEmail(String recipientEmail, String subject, String body,List<Attachment> attachments) throws IOException, MessagingException, javax.mail.MessagingException {
        Message message = createMessageWithEmail(createEmailWithMultipleAttachments(recipientEmail, subject, body,attachments));
       Message message1= gmailService.users().messages().send("me", message).execute();
       return message1;

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
    private static final JsonFactory JSON_FACTORY = getDefaultInstance();


    private MimeMessage createEmail(String to, String subject, String bodyText) throws MessagingException, javax.mail.MessagingException {
        Properties props = new Properties();
        Session session =Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("your_email@example.com"));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        email.setContent(bodyText, "text/html");

        return email;
    }


    public RedirectView authorize(String authorizationUrl,String clientId,String redirectUri,String[] scopes) throws IOException {
        String scopesString = String.join(" ", scopes);
        String url = authorizationUrl + "?client_id=" + clientId + "&response_type=code" + "&scope=" + scopesString + "&redirect_uri=" + redirectUri;

        return new RedirectView(url);
    }
    private MimeMessage createEmailWithMultipleAttachments(String to, String subject, String bodyText, List<Attachment> attachments) throws MessagingException, IOException, javax.mail.MessagingException {

        if(attachments==null || attachments.isEmpty()) {
            return createEmail(to, subject, bodyText);
        }
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress("your_email@example.com"));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(bodyText, "text/plain");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

            for (Attachment attachment : attachments) {
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                byte[] bytes = Base64.decodeBase64(attachment.getData());
                ByteArrayDataSource mediaContent = new ByteArrayDataSource(bytes, attachment.getContentType());

                attachmentBodyPart.setContent(attachment.getData(), attachment.getContentType());

                attachmentBodyPart.setDataHandler(new DataHandler(mediaContent));
                attachmentBodyPart.setFileName(attachment.getName());
                multipart.addBodyPart(attachmentBodyPart);
            }

            email.setContent(multipart);

        return email;
    }




}
