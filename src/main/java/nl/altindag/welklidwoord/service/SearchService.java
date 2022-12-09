/*
 * Copyright 2017 Thunderberry.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
