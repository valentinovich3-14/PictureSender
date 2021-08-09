/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.sender.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class LaunchRequestHandler implements RequestHandler {

    public static final String SPEECH_TEXT = "Welcome to the Picture Sender.";
    public static final String REPROMPT_TEXT = "Please what do you want to send and to whom by saying, sent kittens to Nick";
    public static final String PICTURE_SESSION = "PictureSession";

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = SPEECH_TEXT;
        return input.getResponseBuilder()
                .withSimpleCard(PICTURE_SESSION, speechText)
                .withSpeech(speechText)
                .withReprompt(REPROMPT_TEXT)
                .build();
    }
}
