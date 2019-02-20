package nl.altindag.welklidwoord.service;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.altindag.welklidwoord.model.Lidwoord;

@Service
public class WoordenService implements SearchService {

    private static final String URL = "https://www.woorden.org/woord/";
    private static final String LIDWOORD_ELEMENT_ATTRIBUTE_KEY = "inline";
    private static final Pattern PATTERN = Pattern.compile(LIDWOORD);

    private ClientHelper clientHelper;

    @Autowired
    public WoordenService(ClientHelper clientHelper) {
        this.clientHelper = clientHelper;
    }

    @Override
    public CompletableFuture<Lidwoord> getLidwoord(String zelfstandigNaamwoord) {
        return CompletableFuture.supplyAsync(() -> clientHelper.createRequest(URL + zelfstandigNaamwoord))
                .thenApply(clientHelper::getResponse)
                .thenApply(HttpResponse::body)
                .thenApply(Jsoup::parse)
                .thenApply(document -> extractLidwoord(document, elements -> elements.getElementsByClass(LIDWOORD_ELEMENT_ATTRIBUTE_KEY), this::getLidwoordFromValue));
    }

    private String getLidwoordFromValue(String raw) {
        Matcher matcher = PATTERN.matcher(raw);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return raw;
        }
    }
}
