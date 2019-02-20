package nl.altindag.welklidwoord.service;

import static nl.altindag.welklidwoord.model.Field.DEZE_OF_DIT;
import static nl.altindag.welklidwoord.model.Field.DE_OF_HET;
import static nl.altindag.welklidwoord.model.Field.DIE_OF_DAT;
import static nl.altindag.welklidwoord.model.Field.ELK_OF_ELKE;
import static nl.altindag.welklidwoord.model.Field.ONS_OF_ONZE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.altindag.welklidwoord.model.Field;
import nl.altindag.welklidwoord.model.Lidwoord;

@Service
public class LidwoordServiceImpl implements LidwoordService {

    private VanDaleService vanDaleService;
    private WelkLidwoordService welkLidwoordService;
    private WoordenService woordenService;
    private Map<Field, String> container;

    @Autowired
    public LidwoordServiceImpl(VanDaleService vanDaleService, WelkLidwoordService welkLidwoordService, WoordenService woordenService) {
        this.vanDaleService = vanDaleService;
        this.welkLidwoordService = welkLidwoordService;
        this.woordenService = woordenService;
        InitializeContainer();
    }

    private void InitializeContainer() {
        container = new HashMap<>();
        Stream.of(Field.values())
                .forEach(field -> container.put(field, EMPTY));
    }

    @Override
    public CompletableFuture<Lidwoord> getLidwoord(String zelfstandigNaamwoord) {
        return welkLidwoordService.getLidwoord(zelfstandigNaamwoord)
                .exceptionally(exception -> woordenService.getLidwoord(zelfstandigNaamwoord).join())
                .exceptionally(exception -> vanDaleService.getLidwoord(zelfstandigNaamwoord).join());
    }

    @Override
    public Map<Field, String> getFields(Lidwoord lidwoord, String zelfstandigNaamwoord) {
        UnaryOperator<String> mapper = voorvoegsel -> voorvoegsel + " " + zelfstandigNaamwoord;

        container.put(DE_OF_HET, mapper.apply(lidwoord.toString()));
        container.put(DIE_OF_DAT, mapper.apply(getAanwijzendVoornaamwoordVer(lidwoord).toString()));
        container.put(DEZE_OF_DIT, mapper.apply(getAanwijzendVoornaamwoordDichtbij(lidwoord).toString()));
        container.put(ELK_OF_ELKE, mapper.apply(getOnbepaaldVoornaamwoord(lidwoord).toString()));
        container.put(ONS_OF_ONZE, mapper.apply(getBezittelijkVoornaamWoord(lidwoord).toString()));
        return container;
    }

    @Override
    public Map<Field, String> getContainer() {
        return container;
    }
}
