package nl.altindag.welklidwoord.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import nl.altindag.welklidwoord.model.Lidwoord;

@RunWith(MockitoJUnitRunner.class)
public class WoordenServiceShould {

    @Mock
    private ClientHelper clientHelper;
    @InjectMocks
    private WoordenService victim;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void getLidwoord() throws ExecutionException, InterruptedException {
        HttpRequest mockedRequest = mock(HttpRequest.class);
        HttpResponse<String> mockedResponse = mock(HttpResponse.class);

        when(clientHelper.createRequest(anyString())).thenReturn(mockedRequest);
        when(clientHelper.getResponse(any(HttpRequest.class))).thenReturn(mockedResponse);
        when(mockedResponse.body()).thenReturn("<html><head></head><body><h2 class=\"inline\"><font style=\"font-size:8pt\">de </font>boom</h2></body></html>");

        CompletableFuture<Lidwoord> response = victim.getLidwoord("boom");

        assertThat(response).isNotNull();
        assertThat(response.get()).isEqualTo(Lidwoord.DE);
    }

    @Test
    public void throwExceptionWhenNoLidwoordIsFound() throws ExecutionException, InterruptedException {
        expectedEx.expectMessage("Couldn't find the word");

        HttpRequest mockedRequest = mock(HttpRequest.class);
        HttpResponse<String> mockedResponse = mock(HttpResponse.class);

        when(clientHelper.createRequest(anyString())).thenReturn(mockedRequest);
        when(clientHelper.getResponse(any(HttpRequest.class))).thenReturn(mockedResponse);
        when(mockedResponse.body()).thenReturn("<html><head></head><body><h2 class=\"inline\"><font style=\"font-size:8pt\">jhgjhgkiuyiuyhj</font>boom</h2></body></html>");

        victim.getLidwoord("boom").get();
    }

}
