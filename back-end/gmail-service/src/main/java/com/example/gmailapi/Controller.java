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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import entiites.EmailRequest;

import java.io.IOException;
import java.security.GeneralSecurityException;


@RestController
public class Controller {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private EmailService emailService;

    @Value("${gmail.client.id}")
    private String clientId;
    @Value("${gmail.client.clientSecret}")
    private String clientSecret;
    @Value("${gmail.scopes}")
    private String[] SCOPES;
    @Value("${gmail.redirect.path}")
    private String redirectPath;
    @Value("${gmail.auth.url}")
    private String authorizationUrl;

    @GetMapping("/send")
    public Message send(@RequestBody EmailRequest emailRequest, HttpServletRequest httpServletRequest) throws GeneralSecurityException, IOException {
        String token= (String) httpServletRequest.getHeader("accessToken");
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
      @GetMapping("/oauth2callback")
    public String oauthCallback(@RequestParam(value = "code", required = false) String code) throws IOException {
        GoogleTokenResponse tokenResponse = emailService.getToken(code,clientId,clientSecret,redirectPath);
        return tokenResponse.toString();
    }
    @GetMapping("/authorize")
    public RedirectView authorize() {
            try {
                return emailService.authorize(authorizationUrl,clientId,redirectPath,SCOPES);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
}
