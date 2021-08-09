package com.sender.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

import java.io.UnsupportedEncodingException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

//@RunWith(MockitoJUnitRunner.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest({Transport.class, Message.class})
public class EmailSenderTest {

    public static final String EMAIL_FROM = "picture.sender.skill@gmail.com";
    public static final String VALID_ADDRESS = "denys.honcharenko.8@gmail.com";
    public static final String PASSWORD = "adminskill";
    public static final String PERSONAL = "Picture Sender";

    @Mock
    Message message;

    private EmailSender sender;

    @Before
    public void init(){
        sender = new EmailSender(EMAIL_FROM, PASSWORD, PERSONAL);
        mockStatic(Transport.class);
    }

    @Test
    public void positiveTest() throws MessagingException, UnsupportedEncodingException {
        sender.send("subject", "content", "contentType", "emailTo");
    }

    @Test (expected = MessagingException.class) public void messagingExceptionTest() throws MessagingException, UnsupportedEncodingException {
        PowerMockito.spy(Transport.class);
        PowerMockito.doThrow(new MessagingException()).when(Transport.class);
        Transport.send(message);
        sender.send("subject", "content", "contentType", "emailTo");
    }
}
