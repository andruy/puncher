package com.andruy.backend.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PushNotification {
    @Value("${ntfy.url}")
    private String ntfyUrl;
    private int statusCode;
    private HttpClient httpClient;
    private HttpRequest httpRequest;

    public int send(String title, String body) {
        httpClient = HttpClient.newHttpClient();
        httpRequest = HttpRequest.newBuilder()
                                .header("Title", title)
                                .POST(HttpRequest.BodyPublishers.ofString(body))
                                .uri(URI.create(ntfyUrl == null ? System.getProperty("ntfyUrl") : ntfyUrl))
                                .build();

        statusCode = httpClient.sendAsync(httpRequest, BodyHandlers.ofString())
                                .thenApply(HttpResponse::statusCode)
                                .join();

        return statusCode;
    }
}
