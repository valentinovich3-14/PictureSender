package com.sender.sender;

import com.sender.dto.Hit;
import com.sender.exceptions.CantFindPictureException;
import com.sender.exceptions.DontKnowPersonException;
import com.sender.util.EmailSender;
import com.sender.util.PictureDelivery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PictureSenderTest {

    public static final String TO = "denys h";

    @Mock
    private PictureDelivery pictureDelivery;

    @Mock
    private EmailSender emailSender;

    private PictureSender pictureSender;

    @Before
    public void init(){
        pictureSender = new PictureSender(pictureDelivery, emailSender);
    }

    @Test public void positiveTest() throws InterruptedException, IOException, CantFindPictureException, DontKnowPersonException, URISyntaxException, MessagingException {
        given(pictureDelivery.getPicture(anyString())).willReturn(new Hit("url", 100, 100, "url"));
        doNothing().when(emailSender).send(anyString(), anyString(), anyString(), anyString());
        pictureSender.send("qwerty", TO);
        verify(emailSender).send(anyString(), anyString(), anyString(), anyString());
    }

    @Test (expected = InterruptedException.class) public void interruptedExceptionTest() throws InterruptedException, IOException, CantFindPictureException, DontKnowPersonException, URISyntaxException, MessagingException {
        given(pictureDelivery.getPicture(anyString())).willThrow(InterruptedException.class);
        pictureSender.send("qwerty", TO);
    }

    @Test (expected = IOException.class) public void iOExceptionTest() throws InterruptedException, IOException, CantFindPictureException, DontKnowPersonException, URISyntaxException, MessagingException {
        given(pictureDelivery.getPicture(anyString())).willThrow(IOException.class);
        pictureSender.send("qwerty", TO);
    }

    @Test (expected = CantFindPictureException.class) public void cantFindPictureExceptionTest() throws InterruptedException, IOException, CantFindPictureException, DontKnowPersonException, URISyntaxException, MessagingException {
        given(pictureDelivery.getPicture(anyString())).willThrow(CantFindPictureException.class);
        pictureSender.send("qwerty", TO);
    }

    @Test (expected = DontKnowPersonException.class) public void dontKnowPersonExceptionTest() throws InterruptedException, IOException, CantFindPictureException, DontKnowPersonException, URISyntaxException, MessagingException {
        given(pictureDelivery.getPicture(anyString())).willReturn(new Hit("url", 100, 100, "url"));
        pictureSender.send("qwerty", "qwerty");
    }

    @Test (expected = DontKnowPersonException.class) public void dontKnowNickExceptionTest() throws InterruptedException, IOException, CantFindPictureException, DontKnowPersonException, URISyntaxException, MessagingException {
        given(pictureDelivery.getPicture(anyString())).willReturn(new Hit("url", 100, 100, "url"));
        pictureSender.send("qwerty", "nick");
    }

    @Test (expected = URISyntaxException.class) public void uRISyntaxExceptionTest() throws InterruptedException, IOException, CantFindPictureException, DontKnowPersonException, URISyntaxException, MessagingException {
        given(pictureDelivery.getPicture(anyString())).willThrow(URISyntaxException.class);
        pictureSender.send("qwerty", TO);
    }

    @Test (expected = MessagingException.class) public void messagingExceptionTest() throws InterruptedException, IOException, CantFindPictureException, DontKnowPersonException, URISyntaxException, MessagingException {
        given(pictureDelivery.getPicture(anyString())).willReturn(new Hit("url", 100, 100, "url"));
        doThrow(MessagingException.class).when(emailSender).send(anyString(), anyString(), anyString(), anyString());
        pictureSender.send("qwerty", TO);
    }

    @Test public void urlTest() throws InterruptedException, IOException, CantFindPictureException, DontKnowPersonException, URISyntaxException, MessagingException {
        given(pictureDelivery.getPicture(anyString())).willReturn(new Hit(null, 100, 100, "url"));
        doNothing().when(emailSender).send(anyString(), anyString(), anyString(), anyString());
        pictureSender.send("qwerty", TO);
        verify(emailSender).send(anyString(), anyString(), anyString(), anyString());
    }



}
