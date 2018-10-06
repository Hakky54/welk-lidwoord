package nl.altindag.welklidwoord.service;

import nl.altindag.welklidwoord.model.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static nl.altindag.welklidwoord.model.AanwijzendVoornaamwoordDichtbij.DEZE;
import static nl.altindag.welklidwoord.model.AanwijzendVoornaamwoordDichtbij.DIT;
import static nl.altindag.welklidwoord.model.AanwijzendVoornaamwoordVer.DAT;
import static nl.altindag.welklidwoord.model.AanwijzendVoornaamwoordVer.DIE;
import static nl.altindag.welklidwoord.model.BezittelijkVoornaamwoord.ONS;
import static nl.altindag.welklidwoord.model.BezittelijkVoornaamwoord.ONZE;
import static nl.altindag.welklidwoord.model.Lidwoord.DE;
import static nl.altindag.welklidwoord.model.OnbepaaldVoornaamwoord.ELK;
import static nl.altindag.welklidwoord.model.OnbepaaldVoornaamwoord.ELKE;

public interface LidwoordService {

    CompletableFuture<Lidwoord> getLidwoord(String zelfstandigNaamwoord);

    Map<Field, String> getFields(Lidwoord lidwoord, String zelfstandigNaamwoord);

    Map<Field, String> getContainer();

    default AanwijzendVoornaamwoordVer getAanwijzendVoornaamwoordVer(Lidwoord lidwoord) {
        return lidwoord == DE ? DIE : DAT;
    }

    default AanwijzendVoornaamwoordDichtbij getAanwijzendVoornaamwoordDichtbij(Lidwoord lidwoord) {
        return lidwoord == DE ? DEZE : DIT;
    }

    default BezittelijkVoornaamwoord getBezittelijkVoornaamWoord(Lidwoord lidwoord) {
        return lidwoord == DE ? ONZE : ONS;
    }

    default OnbepaaldVoornaamwoord getOnbepaaldVoornaamwoord(Lidwoord lidwoord) {
        return lidwoord == DE ? ELKE : ELK;
    }
}
