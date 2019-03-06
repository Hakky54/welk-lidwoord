package nl.altindag.welklidwoord.service;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.altindag.welklidwoord.model.Lidwoord;

@Service
public class WelkLidwoordService implements SearchService {

    private static final String URL = "https://www.welklidwoord.nl/";
    private static final String LIDWOORD_ELEMENT_ATTRIBUTE_KEY = "span";

    private ClientHelper clientHelper;

    @Autowired
    public WelkLidwoordService(ClientHelper clientHelper) {
        this.clientHelper = clientHelper;
    }

    @Override
    public CompletableFuture<Lidwoord> getLidwoord(String zelfstandigNaamwoord) {
        return clientHelper.getResponse(clientHelper.createRequest(URL + zelfstandigNaamwoord))
                 .thenApply(HttpResponse::body)
                 .thenApply(Jsoup::parse)
                 .thenApply(document -> extractLidwoord(document, elements -> elements.getElementsByTag(LIDWOORD_ELEMENT_ATTRIBUTE_KEY), UnaryOperator.identity()));
    }

}
