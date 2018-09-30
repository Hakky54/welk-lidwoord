package nl.altindag.welklidwoord.service;

import javafx.util.Pair;
import nl.altindag.welklidwoord.model.Lidwoord;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
public class VanDaleService implements SearchService {

    private static final String SCHEME = "http";
    private static final String HOST = "www.vandale.nl";
    private static final String PATH = "/gratis-woordenboek/nederlands/betekenis/";
    private static final String LIDWOORD = "de|het";
    private static final Pair<String, String> LIDWOORD_ELEMENT_ATTRIBUTE = new Pair<>("class", "fq");

    private ClientHelper clientHelper;

    @Autowired
    public VanDaleService(ClientHelper clientHelper) {
        this.clientHelper = clientHelper;
    }

    @Override
    public Optional<Lidwoord> getLidwoord(String zelfstandigNaamwoord) {
        Optional<Lidwoord> lidwoord;

        try {
            String url = buildURL(zelfstandigNaamwoord);
            HttpGet request = clientHelper.createRequest(url);

            Future<HttpResponse> response = clientHelper.getResponse(request);
            String parsedResponse = clientHelper.parseResponse(response.get(2, TimeUnit.SECONDS));

            lidwoord = extractLidwoord(parsedResponse);
        } catch (Exception e) {
            lidwoord = Optional.empty();
        }

        return lidwoord;
    }

    private String buildURL(String word) throws URISyntaxException {
        return new URIBuilder()
                .setScheme(SCHEME)
                .setHost(HOST)
                .setPath(PATH + word)
                .build()
                .toString();
    }

    private Optional<Lidwoord> extractLidwoord(String response) {
        return Jsoup.parse(response).getElementsByAttributeValueContaining(LIDWOORD_ELEMENT_ATTRIBUTE.getKey(), LIDWOORD_ELEMENT_ATTRIBUTE.getValue())
                .stream()
                .map(Element::text)
                .filter(element -> element.matches(LIDWOORD))
                .map(lidwoord -> Lidwoord.valueOf(lidwoord.toUpperCase()))
                .findAny();
    }

}
