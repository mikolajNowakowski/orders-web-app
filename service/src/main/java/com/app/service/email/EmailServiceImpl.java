package com.app.service.email;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    final Mailer mailer;
    final EmailConfiguration emailConfiguration;
    private static final Logger logger = LogManager.getLogger("DebugLogger");

    @Override
    public void send(String receiver, String subject, String htmlContent) {
        var email = EmailBuilder
                .startingBlank()
                .from(emailConfiguration.fromName(), emailConfiguration.fromAddress())
                .withSubject(subject)
                .withHTMLText(htmlContent)
                .to(receiver)
                .buildEmail();
        logger.debug("Sending mail to" + receiver);
        mailer
                .sendMail(email)
                .thenAccept(result -> {});
    }

    @Override
    public void sendWithAttachment(String recipientName, String mailReciver, String subject, String body, String filename, byte[] attachment) {
        var email = EmailBuilder
                .startingBlank()
                .from(emailConfiguration.fromName(), emailConfiguration.fromAddress())
                .to(recipientName, mailReciver)
                .withSubject(subject)
                .withHTMLText(body)
                .withAttachment(filename, attachment, "application/pdf")
                .buildEmail();
        logger.debug("Sending mail to" + mailReciver);
        mailer.sendMail(email).thenAcceptAsync(result -> {});

    }


}
