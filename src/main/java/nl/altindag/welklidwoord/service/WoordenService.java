package nl.altindag.welklidwoord.service;

import nl.altindag.welklidwoord.model.Lidwoord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WoordenService implements SearchService {

    private static final Logger LOGGER = LogManager.getLogger(WoordenService.class);
    private static final String URL = "https://www.woorden.org/woord/";
    private static final String LIDWOORD_ELEMENT_ATTRIBUTE_KEY = "inline";
    private static final Pattern PATTERN = Pattern.compile(LIDWOORD);

    private final ClientHelper clientHelper;

    public WoordenService(ClientHelper clientHelper) {
        this.clientHelper = clientHelper;
    }

    @Override
    public CompletableFuture<Lidwoord> getLidwoord(String zelfstandigNaamwoord) {
        LOGGER.info("sending a request to Woorden.org to get more details for the search term: " + zelfstandigNaamwoord);

        return clientHelper.getResponse(clientHelper.createRequest(URL + zelfstandigNaamwoord))
                .thenApply(HttpResponse::body)
                .thenApply(Jsoup::parse)
                .thenApply(document -> extractLidwoord(document, elements -> elements.getElementsByClass(LIDWOORD_ELEMENT_ATTRIBUTE_KEY), this::getLidwoordFromValue));
    }

    private String getLidwoordFromValue(String raw) {
        String lidwoord = raw;
        Matcher matcher = PATTERN.matcher(raw);

        if (matcher.find()) {
            lidwoord = matcher.group(1);
        }
        return lidwoord;
    }
}
