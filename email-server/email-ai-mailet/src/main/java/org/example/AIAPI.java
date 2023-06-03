package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

    public String getTLDR(String text) throws Exception {
        //http://email-ai.interactwith.us/api-enc4/tldr
        Gson gson =  new Gson();
        String response = getTLDRJson(text);
        LOGGER.info("response was " + response);
        TLDRResponse tldrResponse = gson.fromJson(response, TLDRResponse.class);
        StringBuilder builder = new StringBuilder();
        for (TLDRText tldrText: tldrResponse.info.text) {
            builder.append(tldrText.text);
        }
        return builder.toString();
    }

    private String getTLDRJson(String text) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://email-ai.interactwith.us/api-enc4/tldr?text=" + URLEncoder.encode(text, StandardCharsets.UTF_8)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

}
