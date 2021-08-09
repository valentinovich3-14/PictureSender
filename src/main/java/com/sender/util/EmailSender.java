package com.sender.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailSender {
    private final String emailFrom;
    private final String personal;
    private final Session session;


    public EmailSender(String emailFrom, String password, String personal) {
        this.emailFrom = emailFrom;
        this.personal = personal;

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailFrom, password);
                    }
                });
    }

    public static void main(String[] args) throws UnsupportedEncodingException, MessagingException {
        String emailFrom = "test@implemica.com";
        String password = "BUREWstErTrAwM";
        String personal = "Picture Sender";
        EmailSender emailSender = new EmailSender(emailFrom, password, personal);
        emailSender.send("Subject", "Content", "text/html", "test+picturesender@implemica.com");
    }

    public void send(String subject, String content, String contentType, String emailTo) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailFrom, personal));
        //message.setFrom(new InternetAddress("picture@implemica.com", personal, "charsent"));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(emailTo)
        );
        message.setSubject(subject);
        message.setContent(content, contentType);
        Transport.send(message);
    }
}
