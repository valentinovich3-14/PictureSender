package com.sender;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.sender.handler.*;
import com.sender.util.PictureDelivery;
import com.sender.sender.PictureSender;
import com.sender.util.EmailSender;
import com.sender.util.ClientHttp;

import java.net.http.HttpClient;

public class MainHandler extends SkillStreamHandler {

    public static final String SKILL_ID = "amzn1.ask.skill.469abd6f-1d93-49e3-9fc6-17a4ab953ede";
    public static final String EMAIL_FROM = "test@implemica.com";
    public static final String PASSWORD = "BUREWstErTrAwM";
    public static final String PERSONAL = "Picture Sender";

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new PictureSenderHandler(
                                new PictureSender(
                                        new PictureDelivery(
                                                new ClientHttp(HttpClient.newBuilder()
                                                        .version(HttpClient.Version.HTTP_2)
                                                        .build())),
                                        new EmailSender(EMAIL_FROM, PASSWORD, PERSONAL))),
                        new LaunchRequestHandler(),
                        new CancelandStopIntentHandler(),
                        new SessionEndedRequestHandler(),
                        new HelpIntentHandler(),
                        new FallbackIntentHandler()
                )
                .withSkillId(SKILL_ID)
                .build();
    }

    public MainHandler() {
        super(getSkill());
    }

}
