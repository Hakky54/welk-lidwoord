package nl.altindag.welklidwoord.service;

import nl.altindag.welklidwoord.model.Lidwoord;

import java.util.Optional;

public interface SearchService {

    Optional<Lidwoord> getLidwoord(String zelfstandigNaamwoord);

}
