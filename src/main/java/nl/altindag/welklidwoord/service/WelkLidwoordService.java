package nl.altindag.welklidwoord.service;

import nl.altindag.welklidwoord.model.Lidwoord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

@Service
public class WelkLidwoordService implements SearchService {

    private static final Logger LOGGER = LogManager.getLogger(WelkLidwoordService.class);
    private static final String URL = "https://www.welklidwoord.nl/";
    private static final String LIDWOORD_ELEMENT_ATTRIBUTE_KEY = "span";

    private ClientHelper clientHelper;

    @Autowired
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
