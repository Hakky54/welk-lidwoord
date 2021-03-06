package nl.altindag.welklidwoord.service;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import nl.altindag.welklidwoord.model.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static nl.altindag.welklidwoord.model.Field.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
public class LidwoordService {

    private final LidwoordOpvraagService service;
    private Alert aboutScreen;

    private final SimpleStringProperty lidwoord = new SimpleStringProperty(DE_OF_HET.toString());
    private final SimpleStringProperty aanwijzendVoornaamwoordVer = new SimpleStringProperty(DIE_OF_DAT.toString());
    private final SimpleStringProperty aanwijzendVoornaamwoordDichtbij = new SimpleStringProperty(DEZE_OF_DIT.toString());
    private final SimpleStringProperty bezittelijkVoornaamwoordOns = new SimpleStringProperty(ONS_OF_ONZE.toString());
    private final SimpleStringProperty onbepaaldVoornaamwoord = new SimpleStringProperty(ELK_OF_ELKE.toString());

    @Autowired
    public LidwoordService(LidwoordOpvraagService service) {
        this.service = service;

        Platform.runLater(() -> {
            aboutScreen = new Alert(Alert.AlertType.INFORMATION);
            aboutScreen.setTitle("Over Welk Lidwoord");
            aboutScreen.setHeaderText("Over Welk Lidwoord");
            aboutScreen.setContentText("App versie 1.0\nGemaakt door Hakan Altındağ");
        });
    }

    public Alert getAboutScreen() {
        return aboutScreen;
    }

    public void search(String zelfstandigNaamwoord) {
        service.getLidwoord(zelfstandigNaamwoord)
               .thenApply(lidwoord -> service.getFields(lidwoord, zelfstandigNaamwoord))
               .exceptionally(exception -> this.getFieldsForError(exception.getCause().getMessage()))
               .thenAccept(this::setAllFields);
    }

    private void setAllFields(Map<Field, String> container) {
        Platform.runLater(() -> {
            lidwoord.set(container.get(DE_OF_HET));
            aanwijzendVoornaamwoordVer.set(container.get(DIE_OF_DAT));
            aanwijzendVoornaamwoordDichtbij.set(container.get(DEZE_OF_DIT));
            bezittelijkVoornaamwoordOns.set(container.get(ONS_OF_ONZE));
            onbepaaldVoornaamwoord.set(container.get(ELK_OF_ELKE));
        });
    }

    private Map<Field, String> getFieldsForError(String message) {
        Map<Field, String> container = service.getContainer();
        container.replaceAll((key, value) -> EMPTY);
        container.put(DEZE_OF_DIT, message);
        return container;
    }

    public SimpleStringProperty lidwoordProperty() {
        return lidwoord;
    }

    public SimpleStringProperty aanwijzendVoornaamwoordVerProperty() {
        return aanwijzendVoornaamwoordVer;
    }

    public SimpleStringProperty aanwijzendVoornaamwoordDichtbijProperty() {
        return aanwijzendVoornaamwoordDichtbij;
    }

    public SimpleStringProperty bezittelijkVoornaamwoordOnsProperty() {
        return bezittelijkVoornaamwoordOns;
    }

    public SimpleStringProperty onbepaaldVoornaamwoordProperty() {
        return onbepaaldVoornaamwoord;
    }
}
