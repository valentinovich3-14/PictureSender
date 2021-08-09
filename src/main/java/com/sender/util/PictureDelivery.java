package com.sender.util;


import com.google.gson.Gson;
import com.sender.dto.Hit;
import com.sender.dto.Json;
import com.sender.exceptions.CantFindPictureException;
import com.sender.util.ClientHttp;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PictureDelivery {

    private static final String URL = "https://pixabay.com/api/?";
    private static final String SEARCH_QUERY_KEY = "q";

    private static final BasicNameValuePair key = new BasicNameValuePair("key", "22379469-793225d6fc918016b76496c82");
    private static final BasicNameValuePair IMAGE_TYPE = new BasicNameValuePair("image_type", "photo");
    private static final BasicNameValuePair EDITORS_CHOICE = new BasicNameValuePair("editors_choice", "true");

    private final ClientHttp httpClient;

    public PictureDelivery(ClientHttp httpClient){
        this.httpClient = httpClient;
    }

    public Hit getPicture(String searchQuery) throws InterruptedException, IOException, URISyntaxException, CantFindPictureException {
        if(Objects.isNull(searchQuery)){
            searchQuery = "";
        }

        List<NameValuePair> parameters = buildParameters(searchQuery);
        String jsonString = httpClient.sendGet(URL, parameters);

        Gson g = new Gson();
        Json json = g.fromJson(jsonString, Json.class);

        Hit[] hits = json.getHits();

        return getHit(hits);
    }

    private List<NameValuePair> buildParameters(String searchQuery) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(key);
        parameters.add(new BasicNameValuePair(SEARCH_QUERY_KEY, formatQuery(searchQuery)));
        parameters.add(IMAGE_TYPE);
        parameters.add(EDITORS_CHOICE);
        return parameters;
    }

    private Hit getHit(Hit[] hits) throws CantFindPictureException {
        Hit hit;
        if(hits.length > 0){
            hit = hits[0];
        }else {
            throw new CantFindPictureException();
        }
        return hit;
    }

    private String formatQuery(String searchQuery) {
        return searchQuery.replace(" ", "+");
    }
}
