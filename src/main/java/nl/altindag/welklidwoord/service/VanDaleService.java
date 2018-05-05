package nl.altindag.welklidwoord.service;

import javafx.util.Pair;
import nl.altindag.welklidwoord.exception.WLException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.UnaryOperator;

import static nl.altindag.welklidwoord.model.Field.*;

public class VanDaleService extends AbstractService<Map<String, String>> {

    private static final String SCHEME = "http";
    private static final String HOST = "www.vandale.nl";
    private static final String PATH = "/gratis-woordenboek/nederlands/betekenis/";

    private static final String DE = "de";
    private static final String DEZE = "deze";
    private static final String DIT = "dit";
    private static final String DIE = "die";
    private static final String DAT = "dat";
    private static final String ONZE = "onze";
    private static final String ONS = "ons";
    private static final String ELKE = "elke";
    private static final String ELK = "elk";

    private static final String LIDWOORD = "de|het";
    private static final Pair<String, String> LIDWOORD_ELEMENT_ATTRIBUTE = new Pair<>("class", "fq");

    @Override
    public Map<String, String> get(String word) throws URISyntaxException, IOException, InterruptedException, ExecutionException, TimeoutException, WLException {
        String url = buildURL(word);
        HttpGet request = createRequest(url);

        Future<HttpResponse> response = getResponse(request);
        String parsedResponse = parseResponse(response.get(1500, TimeUnit.MILLISECONDS));

        request.releaseConnection();

        String lidwoord = getLidwoord(parsedResponse)
                .orElseThrow(() -> new WLException("Woord niet gevonden :("));

        return getAllFields(lidwoord, word);
    }

    private String buildURL(String word) throws URISyntaxException {
        return new URIBuilder()
                .setScheme(SCHEME)
                .setHost(HOST)
                .setPath(PATH + word)
                .build()
                .toString();
    }

    private Optional<String> getLidwoord(String result) {
        return Jsoup.parse(result).getElementsByAttributeValueContaining(LIDWOORD_ELEMENT_ATTRIBUTE.getKey(), LIDWOORD_ELEMENT_ATTRIBUTE.getValue())
                    .stream()
                    .map(Element::text)
                    .filter(element -> element.matches(LIDWOORD))
                    .findFirst();
    }

    private Map<String, String> getAllFields(String lidwoord, final String word) {
        UnaryOperator<String> append = aWord -> aWord + " " + word;
        HashMap<String, String> container = new HashMap<>();

        container.put(DE_OF_HET, append.apply(lidwoord));
        container.put(DIE_OF_DAT, append.apply(getBetrekkelijkeVoornaamwoordVer(lidwoord)));
        container.put(DEZE_OF_DIT, append.apply(getBetrekkelijkeVoornaamwoordDichtbij(lidwoord)));
        container.put(ONS_OF_ONZE, append.apply(getBetrekkelijkeVoornaamwoordOnsOfOnze(lidwoord)));
        container.put(ELK_OF_ELKE, append.apply(getOnbepaaldVoornaamwoord(lidwoord)));
        return container;
    }

    private String getBetrekkelijkeVoornaamwoordDichtbij(String lidwoord) {
        return lidwoord.equals(DE) ? DEZE : DIT;
    }

    private String getBetrekkelijkeVoornaamwoordVer(String lidwoord) {
        return lidwoord.equals(DE) ? DIE : DAT;
    }

    private String getBetrekkelijkeVoornaamwoordOnsOfOnze(String lidwoord) {
        return lidwoord.equals(DE) ? ONZE : ONS;
    }

    private String getOnbepaaldVoornaamwoord(String lidwoord) {
        return lidwoord.equals(DE) ? ELKE : ELK;
    }


}
