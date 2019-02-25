package nl.altindag.welklidwoord.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

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
    public void getResponse() throws IOException, InterruptedException {
        HttpRequest mockedRequest = mock(HttpRequest.class);
        HttpResponse mockedResponse = mock(HttpResponse.class);

        when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockedResponse);

        HttpResponse<String> response = victim.getResponse(mockedRequest);
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(mockedResponse);
    }

    @Test
    public void throwExceptionWhenClientCouldNotParseResponse() throws IOException, InterruptedException {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Could not get the response");
        HttpRequest mockedRequest = mock(HttpRequest.class);

        when(client.send(any(), any())).thenThrow(new IOException());

        victim.getResponse(mockedRequest);
    }

    @Test
    public void throwExceptionWhenClientTimesOut() throws IOException, InterruptedException {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Could not get the response");
        HttpRequest mockedRequest = mock(HttpRequest.class);

        when(client.send(any(), any())).thenThrow(new InterruptedException());

        victim.getResponse(mockedRequest);
    }


}
