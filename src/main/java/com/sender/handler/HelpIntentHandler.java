
package com.sender.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class HelpIntentHandler implements RequestHandler {

    public static final String SPEECH_TEXT = "You can tell what do you want to send and to whom by saying, sent kittens to Nick";
    public static final String REPROMPT_TEXT = "Please tell what do you want to send and to whom by saying, sent kittens to Nick";
    public static final String PICTURE_SESSION = "PictureSession";

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.HelpIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = SPEECH_TEXT;
        return input.getResponseBuilder()
                .withSimpleCard(PICTURE_SESSION, speechText)
                .withSpeech(speechText)
                .withReprompt(REPROMPT_TEXT)
                .withShouldEndSession(false)
                .build();
    }
}
