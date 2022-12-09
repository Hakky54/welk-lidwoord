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
