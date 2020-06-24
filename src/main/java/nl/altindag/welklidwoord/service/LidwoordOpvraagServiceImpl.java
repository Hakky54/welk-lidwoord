package nl.altindag.welklidwoord.service;

import nl.altindag.welklidwoord.model.Field;
import nl.altindag.welklidwoord.model.Lidwoord;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static nl.altindag.welklidwoord.model.Field.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
public class LidwoordOpvraagServiceImpl implements LidwoordOpvraagService {

    private final VanDaleService vanDaleService;
    private final WelkLidwoordService welkLidwoordService;
    private final WoordenService woordenService;
    private final Map<Field, String> container = new EnumMap<>(Field.class);

    public LidwoordOpvraagServiceImpl(VanDaleService vanDaleService, WelkLidwoordService welkLidwoordService, WoordenService woordenService) {
        this.vanDaleService = vanDaleService;
        this.welkLidwoordService = welkLidwoordService;
        this.woordenService = woordenService;
        Stream.of(Field.values()).forEach(field -> container.put(field, EMPTY));
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
