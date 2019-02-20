package nl.altindag.welklidwoord.service;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.altindag.welklidwoord.model.Lidwoord;

@Service
public class WelkLidwoordService implements SearchService {

    private static final String URL = "https://www.welklidwoord.nl/";
    private static final String LIDWOORD = "de|het";
    private static final String LIDWOORD_ELEMENT_ATTRIBUTE_KEY = "span";

    private ClientHelper clientHelper;

    @Autowired
    public WelkLidwoordService(ClientHelper clientHelper) {
        this.clientHelper = clientHelper;
    }

    @Override
    public CompletableFuture<Lidwoord> getLidwoord(String zelfstandigNaamwoord) {
        return CompletableFuture.supplyAsync(() -> clientHelper.createRequest(URL + zelfstandigNaamwoord))
                         .thenApply(request -> clientHelper.getResponse(request))
                         .thenApply(HttpResponse::body)
                         .thenApply(this::extractLidwoord);
    }

    private Lidwoord extractLidwoord(String response) {
        return Jsoup.parse(response).getElementsByTag(LIDWOORD_ELEMENT_ATTRIBUTE_KEY).stream()
                    .map(Element::text)
                    .map(String::toLowerCase)
                    .filter(element -> element.matches(LIDWOORD))
                    .map(lidwoord -> Lidwoord.valueOf(lidwoord.toUpperCase()))
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("Couldn't find the word"));
    }
}
