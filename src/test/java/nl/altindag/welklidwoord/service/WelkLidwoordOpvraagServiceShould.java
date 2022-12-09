/*
 * Copyright 2017 Thunderberry.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.altindag.welklidwoord.service;

import nl.altindag.log.LogCaptor;
import nl.altindag.welklidwoord.model.Lidwoord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WelkLidwoordOpvraagServiceShould {

    @Mock
    private ClientHelper clientHelper;
    @InjectMocks
    private WelkLidwoordService victim;

    @Test
    @SuppressWarnings("unchecked")
    public void getLidwoord() {
        var logCaptor = LogCaptor.forClass(WelkLidwoordService.class);
        var mockedRequest = mock(HttpRequest.class);
        var mockedResponse = (HttpResponse<String>) mock(HttpResponse.class);
        var completedFuture = CompletableFuture.completedFuture(mockedResponse);

        when(clientHelper.createRequest(anyString())).thenReturn(mockedRequest);
        when(clientHelper.getResponse(any(HttpRequest.class))).thenReturn(completedFuture);
        when(mockedResponse.body()).thenReturn("<html><head></head><body><span>De</span></body></html>");

        var response = victim.getLidwoord("boom");

        assertThat(response).isNotNull();
        assertThat(response.join()).isEqualTo(Lidwoord.DE);

        assertThat(logCaptor.getInfoLogs())
                .hasSize(1)
                .contains("sending a request to Welklidwoord.nl to get more details for the search term: boom");
    }

}
