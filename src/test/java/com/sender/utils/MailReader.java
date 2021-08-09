package com.sender.utils;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.mail.util.MimeMessageParser;
import org.junit.jupiter.api.Test;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.*;

public class MailReader {
    Properties properties = null;
    private final String userName;
    private final String password;
    private String htmlContent;

    public MailReader(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public void deleteMessageBySubject(String subject){
        Message message;
        properties = new Properties();
        properties.setProperty("mail.store.protocol", "imaps");
        try {

            Session session = Session.getDefaultInstance(properties, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", userName, password);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            int countMassages = inbox.getMessageCount();

            Message[] messages = inbox.getMessages(countMassages - 9, countMassages);
            List<Message> list = Arrays.asList(messages.clone());
            message = list.stream().filter(m -> {
                try {
                    return m.getSubject().compareTo(subject) == 0;
                } catch (MessagingException e) {
                    return false;
                }
            }).max(Comparator.comparing(m -> {
                try {
                    return m.getReceivedDate();
                } catch (MessagingException e) {
                    return null;
                }
            })).orElse(null);

            assert message != null;
            message.setFlag(Flags.Flag.DELETED, true);



            // disconnect
            inbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for pop3.");
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message getMailBySubject(String subject){
        Message message = null;
        properties = new Properties();
        properties.setProperty("mail.store.protocol", "imaps");
        try {

            Session session = Session.getDefaultInstance(properties, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", userName, password);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            int countMassages = inbox.getMessageCount();

            Message[] messages = inbox.getMessages(countMassages - 9, countMassages);
            List<Message> list = Arrays.asList(messages.clone());
            message = list.stream().filter(m -> {
                try {
                    return m.getSubject().compareTo(subject) == 0;
                } catch (MessagingException e) {
                    return false;
                }
            }).max(Comparator.comparing(m -> {
                try {
                    return m.getReceivedDate();
                } catch (MessagingException e) {
                    return null;
                }
            })).orElse(null);

            htmlContent = new MimeMessageParser((MimeMessage) message).parse().getHtmlContent();

            // disconnect
            inbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for pop3.");
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public String getHtmlContent() {
        return htmlContent;
    }
}