package com.sender.util;

import com.sender.dto.Hit;
import com.sender.exceptions.CantFindPictureException;
import com.sender.util.ClientHttp;
import com.sender.util.PictureDelivery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;


@RunWith(MockitoJUnitRunner.class)
public class PictureDeliveryTest {

    public static final String JSON = "{\n" +
            "    \"total\": 2,\n" +
            "    \"totalHits\": 2,\n" +
            "    \"hits\": [\n" +
            "        {\n" +
            "            \"largeImageURL\": \"https://pixabay.com/get/gb5fd838a.jpg\",\n" +
            "            \"imageWidth\": 2165,\n" +
            "            \"imageHeight\": 1453\n" +
            "        },\n" +
            "        {\n" +
            "            \"largeImageURL\": \"https://pixabay.com/get/gbd28a5b890520ea3bfc9e23f2dcc5e92f0d161d59cf652ef7990076191b9b881026fe9cb600c0dd57a773998007e1d9793ad2a0146ef1938c37e07957a4ba8bc_1280.jpg\",\n" +
            "            \"imageWidth\": 5760,\n" +
            "            \"imageHeight\": 3840\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    public static final String EMPTY_JSON = "{\n" +
            "    \"total\": 0,\n" +
            "    \"totalHits\": 0,\n" +
            "    \"hits\": []\n" +
            "}";


    @Mock
    private ClientHttp client;

    private PictureDelivery delivery;

    @Before
    public void init(){
        delivery = new PictureDelivery(client);
    }

    @Test public void test() throws InterruptedException, IOException, CantFindPictureException, URISyntaxException {
        a(JSON, "qwerty", new Hit("https://pixabay.com/get/gb5fd838a.jpg", 2165, 1453, "https://pixabay.com/get/gb5fd838a.jpg"));
        a(JSON, null, new Hit("https://pixabay.com/get/gb5fd838a.jpg", 2165, 1453, "https://pixabay.com/get/gb5fd838a.jpg"));
        a(JSON, "cat dog", new Hit("https://pixabay.com/get/gb5fd838a.jpg", 2165, 1453, "https://pixabay.com/get/gb5fd838a.jpg"));
        aNotEqual(JSON, "qwerty", new Hit("https://pixabay.com/get/gb5fd838a.jpg", 2166, 1453, "https://pixabay.com/get/gb5fd838a.jpg"));
    }

    @Test (expected = InterruptedException.class) public void interruptedExceptionTest() throws InterruptedException, IOException, URISyntaxException, CantFindPictureException {
        given(client.sendGet(anyString(), any(List.class))).willThrow(InterruptedException.class);
        delivery.getPicture("qwerty");
    }

    @Test (expected = IOException.class) public void iOExceptionTest() throws InterruptedException, IOException, URISyntaxException, CantFindPictureException {
        given(client.sendGet(anyString(), any(List.class))).willThrow(IOException.class);
        delivery.getPicture("qwerty");
    }

    @Test (expected = URISyntaxException.class) public void uRISyntaxExceptionTest() throws InterruptedException, IOException, URISyntaxException, CantFindPictureException {
        given(client.sendGet(anyString(), any(List.class))).willThrow(URISyntaxException.class);
        delivery.getPicture("qwerty");
    }

    @Test (expected = CantFindPictureException.class) public void cantFindPictureExceptionTest() throws InterruptedException, IOException, URISyntaxException, CantFindPictureException {
        given(client.sendGet(anyString(), any(List.class))).willReturn(EMPTY_JSON);
        delivery.getPicture("qwerty");
    }


    private void a(String json, String query, Hit expectedHit) throws InterruptedException, URISyntaxException, CantFindPictureException, IOException {
        Hit actualHit = a(json, query);
        Assert.assertEquals(actualHit, expectedHit);
    }

    private Hit a(String json, String query) throws IOException, InterruptedException, URISyntaxException, CantFindPictureException {
        given(client.sendGet(anyString(), any(List.class))).willReturn(json);
        return delivery.getPicture(query);
    }

    private void aNotEqual(String json, String query, Hit expectedHit) throws InterruptedException, URISyntaxException, CantFindPictureException, IOException {
        Hit actualHit = a(json, query);
        Assert.assertNotEquals(actualHit, expectedHit);
    }
}
