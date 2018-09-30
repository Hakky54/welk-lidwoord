package nl.altindag.welklidwoord.service;

import nl.altindag.welklidwoord.model.Field;
import nl.altindag.welklidwoord.model.Lidwoord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

@Service
public class LidwoordServiceImpl implements LidwoordService {

    private SearchService searchService;
    private Map<Field, String> container = new HashMap<>();

    @Autowired
    public LidwoordServiceImpl(SearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    public Optional<Lidwoord> getLidwoord(String zelfstandigNaamwoord) {
        return searchService.getLidwoord(zelfstandigNaamwoord);
    }

    @Override
    public Map<Field, String> getFields(Lidwoord lidwoord, String zelfstandigNaamwoord) {
        UnaryOperator<String> mapper = voorvoegsel -> voorvoegsel + " " + zelfstandigNaamwoord;

        container.put(Field.DE_OF_HET, mapper.apply(lidwoord.toString()));
        container.put(Field.DIE_OF_DAT, mapper.apply(getAanwijzendVoornaamwoordVer(lidwoord).toString()));
        container.put(Field.DEZE_OF_DIT, mapper.apply(getAanwijzendVoornaamwoordDichtbij(lidwoord).toString()));
        container.put(Field.ELK_OF_ELKE, mapper.apply(getOnbepaaldVoornaamwoord(lidwoord).toString()));
        container.put(Field.ONS_OF_ONZE, mapper.apply(getBezittelijkVoornaamWoord(lidwoord).toString()));
        return container;
    }

}
