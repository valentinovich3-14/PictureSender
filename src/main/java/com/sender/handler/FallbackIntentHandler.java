package com.sender.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class FallbackIntentHandler implements RequestHandler {

    public static final String AMAZON_FALLBACK_INTENT = "AMAZON.FallbackIntent";
    public static final String SPEECH_TEXT = "Sorry, I don't know that. You can say try saying help!";
    public static final String PICTURE_SESSION = "PictureSession";

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(AMAZON_FALLBACK_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = SPEECH_TEXT;
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard(PICTURE_SESSION, speechText)
                .withReprompt(speechText)
                .build();
    }

}
