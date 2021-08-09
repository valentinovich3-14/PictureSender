package com.sender.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.model.slu.entityresolution.Resolution;
import com.amazon.ask.model.ui.Image;
import com.amazon.ask.model.ui.StandardCard;
import com.amazon.ask.response.ResponseBuilder;
import com.sender.dto.Hit;
import com.sender.exceptions.CantFindPictureException;
import com.sender.exceptions.DontKnowPersonException;
import com.sender.sender.PictureSender;


import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static com.amazon.ask.request.Predicates.intentName;

public class PictureSenderHandler implements RequestHandler {

    private static final String NAME_SLOT = "name";
    private static final String PICTURE_SLOT = "picture";
    public static final String INTENT_NAME = "PictureSenderIntent";
    public static final String SPEECH_TEXT = "The %s was sent to %s.";
    public static final String PICTURE_SESSION = "PictureSession";
    public static final String EXCEPTION_PICTURE_SERVICE_SPEECH_TEXT = "I can`t do this. The picture service is temporarily unavailable. Sorry for the inconvenience";
    public static final String DONT_KNOW_PERSON_EXCEPTION_SPEECH_TEXT = "Sorry, but I don`t know %s";
    public static final String CANT_FIND_PICTURE_EXCEPTION_SPEECH_TEXT = "Sorry, but I cannot find the picture on request %s";
    public static final String CANT_SEND_EMAIL_EXCEPTION_SPEECH_TEXT = "Sorry but I can't send email.";

    private final PictureSender pictureSender;

    public PictureSenderHandler(PictureSender pictureSender) {
        this.pictureSender = pictureSender;
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(INTENT_NAME));
    }


    @Override
    public Optional<Response> handle(HandlerInput input) {
        ResponseBuilder builder = input.getResponseBuilder();
        String speechText;
        Hit hit;
        StandardCard standardCard;

        Map<String, Slot> slots = getSlotMap(input);
        String picture = getPicture(slots);
        String to = getName(slots);
        String id = getId(slots);


        try {

            hit = pictureSender.send(picture, id);

            speechText = String.format(SPEECH_TEXT, picture, to);

            standardCard = buildCard(hit, to);

            builder.withCard(standardCard);

        } catch (IOException | URISyntaxException | InterruptedException e) {
            Thread.currentThread().interrupt();
            speechText = EXCEPTION_PICTURE_SERVICE_SPEECH_TEXT;
            builder.withSimpleCard(PICTURE_SESSION, speechText);
        } catch (DontKnowPersonException e) {
            speechText = String.format(DONT_KNOW_PERSON_EXCEPTION_SPEECH_TEXT, to);
            builder.withSimpleCard(PICTURE_SESSION, speechText);
        } catch (CantFindPictureException e) {
            speechText = String.format(CANT_FIND_PICTURE_EXCEPTION_SPEECH_TEXT, picture);
            builder.withSimpleCard(PICTURE_SESSION, speechText);
        } catch (MessagingException e) {
            speechText = CANT_SEND_EMAIL_EXCEPTION_SPEECH_TEXT;
            builder.withSimpleCard(PICTURE_SESSION, speechText);
        }


        return builder
                .withSpeech(speechText)
                .build();
    }

    private StandardCard buildCard(Hit hit, String to) {
        Image image = Image.builder()
                .withSmallImageUrl(hit.getImageURL())
                .withLargeImageUrl(hit.getLargeImageURL())
                .build();

        return StandardCard.builder()
                .withTitle("Success")
                .withText("This picture was send to " + to)
                .withImage(image)
                .build();
    }

    private String getName(Map<String, Slot> slots) {
        return slots.get(NAME_SLOT).getValue().toLowerCase().replace(".", "");
    }

    private String getId(Map<String, Slot> slots) {
        List<Resolution> resolutions = slots.get(NAME_SLOT).getResolutions().getResolutionsPerAuthority();
        String returnValue;
        if(resolutions.isEmpty() || resolutions.get(0).getValues().isEmpty()){
            returnValue = getName(slots);
        }else {
            returnValue = resolutions.get(0).getValues().get(0).getValue().getId();
        }
        return returnValue;
    }

    private String getPicture(Map<String, Slot> slots) {
        return slots.get(PICTURE_SLOT).getValue();
    }

    private Map<String, Slot> getSlotMap(HandlerInput input) {
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        return intent.getSlots();
    }
}

