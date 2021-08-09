package com.sender.util;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClientHttp {

    private final HttpClient client;

    public ClientHttp(HttpClient client){
        this.client = client;
    }

    public String sendGet(String url, List<NameValuePair> parameters) throws IOException, InterruptedException, URISyntaxException {

        if (Objects.isNull(url)) {
            url = "";
        }
        if(Objects.isNull(parameters)){
            parameters = new ArrayList<>();
        }

        URI uri = new URIBuilder(url)
                .addParameters(parameters)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
