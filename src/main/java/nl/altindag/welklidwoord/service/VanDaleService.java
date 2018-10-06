package nl.altindag.welklidwoord.service;

import javafx.util.Pair;
import nl.altindag.welklidwoord.model.Lidwoord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Service
public class VanDaleService implements SearchService {

    private static final String URL = "https://www.vandale.nl/gratis-woordenboek/nederlands/betekenis/";
    private static final String LIDWOORD = "de|het";
    private static final Pair<String, String> LIDWOORD_ELEMENT_ATTRIBUTE = new Pair<>("class", "fq");

    private ClientHelper clientHelper;

    @Autowired
    public VanDaleService(ClientHelper clientHelper) {
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
        return Jsoup.parse(response).getElementsByAttributeValueContaining(LIDWOORD_ELEMENT_ATTRIBUTE.getKey(), LIDWOORD_ELEMENT_ATTRIBUTE.getValue())
                .stream()
                .map(Element::text)
                .filter(element -> element.matches(LIDWOORD))
                .map(lidwoord -> Lidwoord.valueOf(lidwoord.toUpperCase()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Could not extract the word"));
    }

}
