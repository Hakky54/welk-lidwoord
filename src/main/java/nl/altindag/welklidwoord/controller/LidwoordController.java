package nl.altindag.welklidwoord.controller;

import static javafx.geometry.Pos.CENTER;
import static nl.altindag.welklidwoord.model.Field.DEZE_OF_DIT;
import static nl.altindag.welklidwoord.model.Field.DE_OF_HET;
import static nl.altindag.welklidwoord.model.Field.DIE_OF_DAT;
import static nl.altindag.welklidwoord.model.Field.ELK_OF_ELKE;
import static nl.altindag.welklidwoord.model.Field.ONS_OF_ONZE;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;

import java.util.Map;

import org.springframework.stereotype.Controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import nl.altindag.welklidwoord.model.Field;
import nl.altindag.welklidwoord.service.LidwoordService;
import nl.altindag.welklidwoord.service.LidwoordServiceImpl;

@Controller
public class LidwoordController {

    @FXML
    private Label aanwijzendVerLabel;
    @FXML
    private Label aanwijzendDichtbijLabel;
    @FXML
    private Label bezittelijkOnsLabel;
    @FXML
    private Label onbepaaldLabel;
    @FXML
    private Label lidwoordLabel;
    @FXML
    private TextField searchField;
    @FXML
    private MenuItem closeMenuItem;
    private LidwoordService service;

    private SimpleStringProperty lidwoord = new SimpleStringProperty(DE_OF_HET.toString());
    private SimpleStringProperty aanwijzendVoornaamwoordVer = new SimpleStringProperty(DIE_OF_DAT.toString());
    private SimpleStringProperty aanwijzendVoornaamwoordDichtbij = new SimpleStringProperty(DEZE_OF_DIT.toString());
    private SimpleStringProperty bezittelijkVoornaamwoordOns = new SimpleStringProperty(ONS_OF_ONZE.toString());
    private SimpleStringProperty onbepaaldVoornaamwoord = new SimpleStringProperty(ELK_OF_ELKE.toString());
    private Alert aboutScreen;

    public LidwoordController(LidwoordServiceImpl service) {
        this.service = service;
    }

    @FXML
    public void initialize() {
        lidwoordLabel.textProperty().bind(lidwoord);
        onbepaaldLabel.textProperty().bind(onbepaaldVoornaamwoord);
        aanwijzendDichtbijLabel.textProperty().bind(aanwijzendVoornaamwoordDichtbij);
        bezittelijkOnsLabel.textProperty().bind(bezittelijkVoornaamwoordOns);
        aanwijzendVerLabel.textProperty().bind(aanwijzendVoornaamwoordVer);

        searchField.setAlignment(CENTER);
        closeMenuItem.setOnAction(e -> Platform.exit());

        searchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.contains(SPACE)) {
                searchField.setText(oldValue);
            }
        });
    }

    @FXML
    public void about(ActionEvent event) {
        getAboutScreen().show();
    }

    private Alert getAboutScreen() {
        if (aboutScreen == null) {
            aboutScreen = new Alert(Alert.AlertType.INFORMATION);
            aboutScreen.setTitle("Over Welk Lidwoord");
            aboutScreen.setHeaderText("Over Welk Lidwoord");
            aboutScreen.setContentText("App versie 1.0");
        }
        return aboutScreen;
    }

    @FXML
    public void onEnter(ActionEvent event) {
        String zelfstandigNaamwoord = searchField.getText().toLowerCase();

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

}
