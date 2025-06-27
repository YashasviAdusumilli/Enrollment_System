package org.example.service;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mail.*;

import io.vertx.ext.mail.StartTLSOptions;

public class EmailService {
    private final MailClient mailClient;

    public EmailService(Vertx vertx) {
        MailConfig mailConfig = new MailConfig()
                .setHostname("smtp.gmail.com")
                .setPort(587)
                .setStarttls(StartTLSOptions.REQUIRED)
                .setUsername("Your_Email_Here")        // 🔁 Replace with your Gmail
                .setPassword("Your_GAPP_PASS");         // 🔁 Replace with App Password

        this.mailClient = MailClient.createShared(vertx, mailConfig);
    }

    public void sendEmail(String to, String subject, String content) {
        MailMessage message = new MailMessage()
                .setFrom("SAME_EMAIL_HERE")           // 🔁 Same as above
                .setTo(to)
                .setSubject(subject)
                .setText(content);

        mailClient.sendMail(message, result -> {
            if (result.succeeded()) {
                System.out.println("Email sent to " + to);
            } else {
                result.cause().printStackTrace();
            }
        });
    }
}
