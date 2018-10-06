package nl.altindag.welklidwoord.service;

import nl.altindag.welklidwoord.model.Lidwoord;

import java.util.concurrent.CompletableFuture;

public interface SearchService {

    CompletableFuture<Lidwoord> getLidwoord(String zelfstandigNaamwoord);

}
