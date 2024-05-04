package com.example.gmailapi;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;
import entiites.AccountCredential;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import entiites.EmailRequest;

import javax.security.auth.login.CredentialNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;


@RestController
@RequestMapping("")
public class Controller {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private EmailService emailService;
/*
{
    "from": "abderrazzak.nouari@gmail.com",
    "subject": "HII",
    "message": "This is a test email",
    "attachments":null
}
 */

    @PostMapping("/send")
    public Message send(@RequestBody EmailRequest emailRequest, HttpServletRequest httpServletRequest) throws GeneralSecurityException, IOException {
        String token= (String) httpServletRequest.getHeader("Authorization");
        if(token==null)
            throw new CredentialNotFoundException("Token not found");
        token=token.substring(7);

        GmailServiceConfig gmailServiceConfig = applicationContext.getBean(GmailServiceConfig.class);
        Gmail gmailService = gmailServiceConfig.gmailService(new AccountCredential(token));
        emailService.setGmailService(gmailService);
        try {
                return emailService.sendEmail(emailRequest.getFrom(), emailRequest.getSubject(), emailRequest.getMessage(),emailRequest.getAttachments());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (javax.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}
