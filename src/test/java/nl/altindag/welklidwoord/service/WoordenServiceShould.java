package nl.altindag.welklidwoord.service;

import ch.qos.logback.classic.Level;
import nl.altindag.log.LogCaptor;
import nl.altindag.welklidwoord.model.Lidwoord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WoordenServiceShould {

    @Mock
    private ClientHelper clientHelper;
    @InjectMocks
    private WoordenService victim;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    @SuppressWarnings("unchecked")
    public void getLidwoord() {
        var logCaptor = LogCaptor.forClass(WoordenService.class);
        var mockedRequest = mock(HttpRequest.class);
        var mockedResponse = (HttpResponse<String>) mock(HttpResponse.class);
        var completedFuture = CompletableFuture.completedFuture(mockedResponse);

        when(clientHelper.createRequest(anyString())).thenReturn(mockedRequest);
        when(clientHelper.getResponse(any())).thenReturn(completedFuture);
        when(mockedResponse.body()).thenReturn("<html><head></head><body><h2 class=\"inline\"><font style=\"font-size:8pt\">de </font>boom</h2></body></html>");

        var response = victim.getLidwoord("boom");

        assertThat(response).isNotNull();
        assertThat(response.join()).isEqualTo(Lidwoord.DE);

        assertThat(logCaptor.getLogs(Level.INFO))
                .hasSize(1)
                .contains("sending a request to Woorden.org to get more details for the search term: boom");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void throwExceptionWhenNoLidwoordIsFound() {
        expectedEx.expectMessage("Ik kon het lidwoord niet vinden");

        var mockedRequest = mock(HttpRequest.class);
        var mockedResponse = (HttpResponse<String>) mock(HttpResponse.class);
        var completedFuture = CompletableFuture.completedFuture(mockedResponse);

        when(clientHelper.createRequest(anyString())).thenReturn(mockedRequest);
        when(clientHelper.getResponse(any(HttpRequest.class))).thenReturn(completedFuture);
        when(mockedResponse.body()).thenReturn("<html><head></head><body><h2 class=\"inline\"><font style=\"font-size:8pt\">jhgjhgkiuyiuyhj</font>boom</h2></body></html>");

        victim.getLidwoord("boom").join();
    }

}
