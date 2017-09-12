package nl.altindag.welklidwoord.service;

import nl.altindag.welklidwoord.exception.WLException;
import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LidWoordService extends AbstractService<Map<String, String>> {

    private static final String URL = "https://www.welklidwoord.nl/";
    private static final String NOT_FOUND = "Helaas, we zijn nog niet zo slim is het wel een zelfstandig naamwoord?";

    public static final String DE_OF_HET = "de-of-het";
    public static final String JOU_OF_JOUW = "jou-of-jouw";
    public static final String ELK_OF_ELKE = "elk-of-elke";
    public static final String DIE_OF_DAT = "die-of-dat";
    public static final String ONS_OF_ONZE = "ons-of-onze";

    public Map<String, String> get(String zelfstandigNaamWoord) throws IOException, WLException {
        setRequest(createRequest(URL + zelfstandigNaamWoord));
        HttpResponse response = getResponse(getRequest());

        String parsedResponse = parseResponse(response);

        return getAllFields(parsedResponse);
    }

    private Map<String, String> getAllFields(String result) throws WLException {
        if (result.contains(NOT_FOUND)) {
            throw new WLException("Niet gevonden  :(");
        }

        Document document = Jsoup.parse(result);

        Map<String, String> container = new HashMap<>();
        Function<String, String> elementFinder = element -> document.getElementsByAttributeValueContaining("href", element).first().text();

        container.put(DE_OF_HET, document.select("h1").text());
        container.put(JOU_OF_JOUW, elementFinder.apply(JOU_OF_JOUW));
        container.put(DIE_OF_DAT, elementFinder.apply(DIE_OF_DAT));
        container.put(ONS_OF_ONZE, elementFinder.apply(ONS_OF_ONZE));
        container.put(ELK_OF_ELKE, elementFinder.apply(ELK_OF_ELKE));

        return container;
    }
}
