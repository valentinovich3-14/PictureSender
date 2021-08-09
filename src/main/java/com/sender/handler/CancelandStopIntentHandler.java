
package com.sender.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class CancelandStopIntentHandler implements RequestHandler {

    public static final String INTENT_NAME = "AMAZON.StopIntent";
    public static final String AMAZON_CANCEL_INTENT = "AMAZON.CancelIntent";
    public static final String SPEECH_TEXT = "Goodbye";
    public static final String CARD_TEXT = "Goodbye";
    public static final String PICTURE_SESSION = "PictureSession";

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(INTENT_NAME).or(intentName(AMAZON_CANCEL_INTENT)));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder()
                .withSpeech(SPEECH_TEXT)
                .withSimpleCard(PICTURE_SESSION, CARD_TEXT)
                .build();
    }
}
