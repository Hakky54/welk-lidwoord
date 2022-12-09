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

import nl.altindag.welklidwoord.model.Lidwoord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

@Service
public class WelkLidwoordService implements SearchService {

    private static final Logger LOGGER = LogManager.getLogger(WelkLidwoordService.class);
    private static final String URL = "https://www.welklidwoord.nl/";
    private static final String LIDWOORD_ELEMENT_ATTRIBUTE_KEY = "span";

    private final ClientHelper clientHelper;

    public WelkLidwoordService(ClientHelper clientHelper) {
        this.clientHelper = clientHelper;
    }

    @Override
    public CompletableFuture<Lidwoord> getLidwoord(String zelfstandigNaamwoord) {
        LOGGER.info("sending a request to Welklidwoord.nl to get more details for the search term: " + zelfstandigNaamwoord);

        return clientHelper.getResponse(clientHelper.createRequest(URL + zelfstandigNaamwoord))
                 .thenApply(HttpResponse::body)
                 .thenApply(Jsoup::parse)
                 .thenApply(document -> extractLidwoord(document, elements -> elements.getElementsByTag(LIDWOORD_ELEMENT_ATTRIBUTE_KEY), UnaryOperator.identity()));
    }

}
