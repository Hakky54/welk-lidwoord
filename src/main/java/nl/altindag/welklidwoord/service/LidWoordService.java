package nl.altindag.welklidwoord.service;

import nl.altindag.welklidwoord.exception.WLException;
import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static nl.altindag.welklidwoord.Field.*;

public class LidWoordService extends AbstractService<Map<String, String>> {

    private static final String URL = "https://www.welklidwoord.nl/";
    private static final String NOT_FOUND = "Helaas, we zijn nog niet zo slim is het wel een zelfstandig naamwoord?";

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

        String[] lidwoord = document.select("h1").text().split(" ");

        container.put(DE_OF_HET, String.join(" ", lidwoord));
        container.put(DIE_OF_DAT, elementFinder.apply(DIE_OF_DAT).substring(elementFinder.apply(DIE_OF_DAT).indexOf(":") + 2));
        container.put(DEZE_OF_DIT, lidwoord[0].equals("de") ? "Deze " + lidwoord[1] : "Dit " + lidwoord[1]);
        container.put(ONS_OF_ONZE, elementFinder.apply(ONS_OF_ONZE).substring(elementFinder.apply(ONS_OF_ONZE).indexOf(":") + 2));
        container.put(ELK_OF_ELKE, elementFinder.apply(ELK_OF_ELKE).substring(elementFinder.apply(ELK_OF_ELKE).indexOf(":") + 2));

        return container;
    }
}
