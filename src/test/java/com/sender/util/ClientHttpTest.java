package com.sender.util;

import org.apache.http.NameValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ClientHttpTest {

    public static final String JSON;

    public static final String INCORRECT_JSON;

    private final static HttpResponse<String> response;

    private static final String URL;

    static {
        URL = "https://pixabay.com/api/?";

        response = new HttpResponse<String>() {
            @Override
            public int statusCode() {
                return 0;
            }

            @Override
            public HttpRequest request() {
                return null;
            }

            @Override
            public Optional<HttpResponse<String>> previousResponse() {
                return Optional.empty();
            }

            @Override
            public HttpHeaders headers() {
                return null;
            }

            @Override
            public String body() {
                return JSON;
            }

            @Override
            public Optional<SSLSession> sslSession() {
                return Optional.empty();
            }

            @Override
            public URI uri() {
                return null;
            }

            @Override
            public HttpClient.Version version() {
                return null;
            }
        };

        JSON = "{\n" +
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

        INCORRECT_JSON = "{\n" +
                "    \"total\": 2,\n" +
                "    \"totalHits\": 2,\n" +
                "    \"hits\": [\n" +
                "        {\n" +
                "            \"largeImageURL\": \"https://pixabay.com/get/gb5fd838a.jpg\",\n" +
                "            \"imageWidth\": 2166,\n" +
                "            \"imageHeight\": 1453\n" +
                "        },\n" +
                "        {\n" +
                "            \"largeImageURL\": \"https://pixabay.com/get/gbd28a5b890520ea3bfc9e23f2dcc5e92f0d161d59cf652ef7990076191b9b881026fe9cb600c0dd57a773998007e1d9793ad2a0146ef1938c37e07957a4ba8bc_1280.jpg\",\n" +
                "            \"imageWidth\": 5760,\n" +
                "            \"imageHeight\": 3840\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    private final List<NameValuePair> nameValuePairs = new ArrayList<>();

    @Mock
    HttpClient httpClient;

    ClientHttp client;

    @Before
    public void init(){
        client = new ClientHttp(httpClient);
    }

    @Test public void positiveTest() throws IOException, InterruptedException, URISyntaxException {
        given(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).willReturn(response);
        String actualJson = client.sendGet(URL, nameValuePairs);
        Assert.assertEquals(JSON, actualJson);
    }

    @Test public void notEqualJsonTest() throws IOException, InterruptedException, URISyntaxException {
        given(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).willReturn(response);
        String actualJson = client.sendGet(URL, nameValuePairs);
        Assert.assertNotEquals(INCORRECT_JSON, actualJson);
    }

    @Test (expected = IOException.class) public void iOExceptionTest() throws IOException, InterruptedException, URISyntaxException {
        given(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).willThrow(IOException.class);
        client.sendGet(URL, nameValuePairs);
    }

    @Test (expected = InterruptedException.class) public void interruptedExceptionTest() throws IOException, InterruptedException, URISyntaxException {
        given(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).willThrow(InterruptedException.class);
        client.sendGet(URL, nameValuePairs);
    }

    @Test (expected = IllegalArgumentException.class) public void illegalArgumentExceptionTest() throws IOException, InterruptedException, URISyntaxException {
        client.sendGet("URL", nameValuePairs);
    }

    @Test (expected = IllegalArgumentException.class) public void nullURLTest() throws IOException, InterruptedException, URISyntaxException {
        client.sendGet(null, nameValuePairs);
    }

    @Test public void nullParametersTest() throws IOException, InterruptedException, URISyntaxException {
        given(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).willReturn(response);
        String actualJson = client.sendGet(URL, null);
        Assert.assertEquals(JSON, actualJson);
    }
}
