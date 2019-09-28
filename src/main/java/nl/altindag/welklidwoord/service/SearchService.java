package nl.altindag.welklidwoord.service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import nl.altindag.welklidwoord.model.Lidwoord;

public interface SearchService {

    String LIDWOORD = ".*(de|het).*";

    CompletableFuture<Lidwoord> getLidwoord(String zelfstandigNaamwoord);

    default Lidwoord extractLidwoord(Document document, Function<Document, Elements> documentMapper, UnaryOperator<String> rawFieldMapper) {
        return documentMapper.apply(document).stream()
                    .map(Element::text)
                    .map(String::toLowerCase)
                    .filter(element -> element.matches(LIDWOORD))
                    .map(rawFieldMapper)
                    .map(lidwoord -> Lidwoord.valueOf(lidwoord.toUpperCase()))
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("Ik kon het lidwoord niet vinden"));
    }

}
