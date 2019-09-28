package nl.altindag.welklidwoord.service;

import static nl.altindag.welklidwoord.model.AanwijzendVoornaamwoordDichtbij.DEZE;
import static nl.altindag.welklidwoord.model.AanwijzendVoornaamwoordDichtbij.DIT;
import static nl.altindag.welklidwoord.model.AanwijzendVoornaamwoordVer.DAT;
import static nl.altindag.welklidwoord.model.AanwijzendVoornaamwoordVer.DIE;
import static nl.altindag.welklidwoord.model.BezittelijkVoornaamwoord.ONS;
import static nl.altindag.welklidwoord.model.BezittelijkVoornaamwoord.ONZE;
import static nl.altindag.welklidwoord.model.Lidwoord.DE;
import static nl.altindag.welklidwoord.model.OnbepaaldVoornaamwoord.ELK;
import static nl.altindag.welklidwoord.model.OnbepaaldVoornaamwoord.ELKE;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import nl.altindag.welklidwoord.model.AanwijzendVoornaamwoordDichtbij;
import nl.altindag.welklidwoord.model.AanwijzendVoornaamwoordVer;
import nl.altindag.welklidwoord.model.BezittelijkVoornaamwoord;
import nl.altindag.welklidwoord.model.Field;
import nl.altindag.welklidwoord.model.Lidwoord;
import nl.altindag.welklidwoord.model.OnbepaaldVoornaamwoord;

public interface LidwoordOpvraagService {

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
