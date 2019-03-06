package nl.altindag.welklidwoord.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClientHelperShould {

    @Mock
    private HttpClient client;
    @InjectMocks
    private ClientHelper victim;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void createRequest() {
        HttpRequest request = victim.createRequest("https://google.com");

        assertThat(request).isNotNull();
        assertThat(request.method()).isEqualTo("GET");
        assertThat(request.uri().toString()).isEqualTo("https://google.com");
        assertThat(request.timeout()).hasValue(Duration.ofSeconds(2));
    }

    @Test
    public void getResponse() {
        HttpRequest mockedRequest = mock(HttpRequest.class);
        HttpResponse mockedResponse = mock(HttpResponse.class);
        CompletableFuture<HttpResponse<String>> completedFuture = CompletableFuture.completedFuture(mockedResponse);

        doReturn(completedFuture).when(client).sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));

        CompletableFuture<HttpResponse<String>> response = victim.getResponse(mockedRequest);
        assertThat(response.join()).isNotNull();
        assertThat(response.join()).isEqualTo(mockedResponse);
    }

}
