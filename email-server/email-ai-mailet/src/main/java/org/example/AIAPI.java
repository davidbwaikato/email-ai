package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class AIAPI {
    public String getTLDR(String text) throws Exception {
        //http://email-ai.interactwith.us/api-enc4/tldr
        Gson gson =  new Gson();
        String response = getTLDRJson(text);
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
