package nl.altindag.welklidwoord.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientHelperShould {

    @Mock
    private HttpClient client;
    @InjectMocks
    private ClientHelper victim;

    @Test
    public void createRequest() {
        HttpRequest request = victim.createRequest("https://google.com");

        assertThat(request).isNotNull();
        assertThat(request.method()).isEqualTo("GET");
        assertThat(request.uri().toString()).isEqualTo("https://google.com");
        assertThat(request.timeout()).hasValue(Duration.ofSeconds(2));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getResponse() {
        var mockedRequest = mock(HttpRequest.class);
        var mockedResponse = (HttpResponse<String>) mock(HttpResponse.class);
        var completedFuture = CompletableFuture.completedFuture(mockedResponse);

        when(client.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(completedFuture);

        var response = victim.getResponse(mockedRequest);
        assertThat(response.join()).isNotNull();
        assertThat(response.join()).isEqualTo(mockedResponse);
    }

}
