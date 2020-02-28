package nl.altindag.welklidwoord.service;

import javafx.util.Pair;
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
public class VanDaleService implements SearchService {

    private static final Logger LOGGER = LogManager.getLogger(VanDaleService.class);
    private static final String URL = "https://www.vandale.nl/gratis-woordenboek/nederlands/betekenis/";
    private static final Pair<String, String> LIDWOORD_ELEMENT_ATTRIBUTE = new Pair<>("class", "fq");

    private ClientHelper clientHelper;

    @Autowired
    public VanDaleService(ClientHelper clientHelper) {
        this.clientHelper = clientHelper;
    }

    @Override
    public CompletableFuture<Lidwoord> getLidwoord(String zelfstandigNaamwoord) {
        LOGGER.info("sending a request to VanDale.nl to get more details for the search term: " + zelfstandigNaamwoord);

        return clientHelper.getResponse(clientHelper.createRequest(URL + zelfstandigNaamwoord))
                .thenApply(HttpResponse::body)
                .thenApply(Jsoup::parse)
                .thenApply(document -> extractLidwoord(document,
                                                       elements -> elements.getElementsByAttributeValueContaining(LIDWOORD_ELEMENT_ATTRIBUTE.getKey(), LIDWOORD_ELEMENT_ATTRIBUTE.getValue()),
                                                       UnaryOperator.identity()));
    }

}
