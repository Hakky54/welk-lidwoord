package nl.altindag.welklidwoord.service;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Service
public class ClientHelper {

    private final HttpClient client;

    public ClientHelper(HttpClient client) {
        this.client = client;
    }

    HttpRequest createRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(2))
                .build();
    }

    CompletableFuture<HttpResponse<String>> getResponse(HttpRequest request) {
        return client.sendAsync(request, BodyHandlers.ofString());
    }

}
