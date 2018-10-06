package nl.altindag.welklidwoord.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class ClientHelper {

    private HttpClient client;

    @Autowired
    public ClientHelper(HttpClient client) {
        this.client = client;
    }

    HttpRequest createRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(2))
                .build();
    }

    HttpResponse<String> getResponse(HttpRequest request) {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException("Could not get the response");
        }
    }

}
