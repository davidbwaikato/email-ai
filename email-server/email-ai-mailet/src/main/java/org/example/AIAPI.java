package org.example;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class AIAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AIAPI.class);

    public String getKeywords(String text) throws Exception {
        //http://email-ai.interactwith.us/api-enc4/keywords
        Gson gson =  new Gson();
        String response = getKeywordsJson(text);
        LOGGER.info("response was " + response);
        KeywordsResponse keywordsResponse = gson.fromJson(response, KeywordsResponse.class);
        StringBuilder builder = new StringBuilder();
        for (KeywordsText keywordsText: keywordsResponse.info.keywordsList) {
            builder.append(keywordsText.Keywords);
        }
        return builder.toString();
    }

    private String getKeywordsJson(String text) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://email-ai.interactwith.us/api-enc4/keywords?text=" + URLEncoder.encode(text, StandardCharsets.UTF_8)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

}
