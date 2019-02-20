package nl.altindag.welklidwoord.service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import nl.altindag.welklidwoord.model.Lidwoord;

public interface SearchService {

    String LIDWOORD = "de|het";

    CompletableFuture<Lidwoord> getLidwoord(String zelfstandigNaamwoord);

    default Lidwoord extractLidwoord(Document document, Function<Document, Elements> mapper) {
        return mapper.apply(document).stream()
                    .map(Element::text)
                    .map(String::toLowerCase)
                    .filter(element -> element.matches(LIDWOORD))
                    .map(lidwoord -> Lidwoord.valueOf(lidwoord.toUpperCase()))
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("Couldn't find the word"));
    }

}
