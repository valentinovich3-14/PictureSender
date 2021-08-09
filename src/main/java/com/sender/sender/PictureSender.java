package com.sender.sender;

import com.sender.dto.Hit;
import com.sender.exceptions.DontKnowPersonException;
import com.sender.exceptions.CantFindPictureException;
import com.sender.util.PictureDelivery;
import com.sender.util.EmailSender;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class PictureSender {

    public static final String LINK_TO_SKILL = "https://skills-store.amazon.com/deeplink/tvt/99400617949a8b030b7f7fb02510ac2d6ae0409f97aaecc8d71e967914d38691a30a8728b818ebbdb4e191c6ce856bcccb022fd46f5a86f5247129bcc732b984f9bc4080f570a94f361b1e8fdb46f1824d98b3a4d09346e5079cc4dc86ae1d02db4ebd3e41ff427286a0897f7688f7";

    public static final String HTML_CONTENT =
            "<h2> Enjoy %s sent with <a href=\"%s\">Picture Sender</a></h2>" +
            "<p><img src=\"%s\" width=\"%s\" height=\"%s\"></p>";
    public static final String CONTENT_TYPE = "text/html";
    public static final String SUBJECT = "Picture from Alexa Skill";
    public static final int STANDARD_WIDTH = 600;
    protected static final Map<String, String> EMPLOYEES = new HashMap<>();

    private final PictureDelivery pictureDelivery;
    private final EmailSender emailSender;

    static {
        EMPLOYEES.put("michaelB", "michael.b@implemica.com");
        EMPLOYEES.put("nikitaG", "nick.g@implemica.com");
        EMPLOYEES.put("denysH", "denys.honcharenko.8@gmail.com");
        EMPLOYEES.put("michailP", "pilipenkomhl@gmail.com");
        EMPLOYEES.put("victoriaC", "victoria.c@implemica.com");
        EMPLOYEES.put("alexK", "alex.h@implemica.com");
        EMPLOYEES.put("vladimirK", "vladimir.k@implemica.com");
        EMPLOYEES.put("tarasT", "tvkanda@gmail.com");
        EMPLOYEES.put("romanK", "roman.k@implemica.com");
        EMPLOYEES.put("mariaK", "maria.k@implemica.com");
        EMPLOYEES.put("dmitriyK", "dmitry.k@implemica.com");
        EMPLOYEES.put("denysG", "denis.g@implemica.com");
        EMPLOYEES.put("dariaS", "daria.s@implemica.com");
        EMPLOYEES.put("alexR", "alex.r@implemica.com");
        EMPLOYEES.put("borisK", "boris.k@implemica.com");
        EMPLOYEES.put("testT", "test+picturesender@implemica.com");
        EMPLOYEES.put("danK", "dan.k@implemica.com");
    }

    public PictureSender (PictureDelivery pictureDelivery, EmailSender emailSender){
        this.pictureDelivery = pictureDelivery;
        this.emailSender = emailSender;
    }

    public Hit send(String picture, String to) throws DontKnowPersonException, InterruptedException, IOException, URISyntaxException, CantFindPictureException, MessagingException {
        Hit hit = pictureDelivery.getPicture(picture);

        formatHit(hit);

        String mailTo = getEmployeeMail(to);

        emailSender.send(SUBJECT, formContent(hit, picture), CONTENT_TYPE, mailTo);

        return hit;
    }

    private void formatHit(Hit hit) {
        if(hit.getImageWidth() > STANDARD_WIDTH) {
            hit.setImageHeight(hit.getImageHeight() / (hit.getImageWidth() / STANDARD_WIDTH));
            hit.setImageWidth(STANDARD_WIDTH);
        }
    }

    private String getEmployeeMail(String to) throws DontKnowPersonException {
        String mailTo;
        if(EMPLOYEES.containsKey(to)){
            mailTo = EMPLOYEES.get(to);
        }else {
            throw new DontKnowPersonException();
        }
        return mailTo;
    }

    private String formContent(Hit hit, String picture){
        return String.format(HTML_CONTENT, picture, LINK_TO_SKILL, hit.getLargeImageURL(), hit.getImageWidth(), hit.getImageHeight());
    }
}
