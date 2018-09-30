package nl.altindag.welklidwoord.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import nl.altindag.welklidwoord.model.Field;
import nl.altindag.welklidwoord.service.ClientHelper;
import nl.altindag.welklidwoord.service.LidwoordServiceImpl;
import nl.altindag.welklidwoord.view.ProxyView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Map;

import static javafx.geometry.Pos.CENTER;
import static nl.altindag.welklidwoord.model.Field.*;
import static nl.altindag.welklidwoord.service.ClientHelper.HTTP_CLIENT_SUPPLIER;

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
    public CheckMenuItem proxyCheckMenuItem;
    @FXML
    private MenuItem proxySettingsMenuItem;
    @FXML
    private MenuItem closeMenuItem;
    @Autowired
    private ProxyView proxyView;
    @Autowired
    private ClientHelper clientHelper;
    private LidwoordServiceImpl service;

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

        proxySettingsMenuItem.setOnAction(e -> proxyView.show());
        proxyCheckMenuItem.selectedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        clientHelper.setClient(HTTP_CLIENT_SUPPLIER);
                    } else {
                        clientHelper.setClient(proxyView.getProxy());
                    }
                });
        Platform.runLater(() -> {
            proxyCheckMenuItem.disableProperty()
                    .bind(proxyView.getFieldsAreEmptyBooleanBinding());

        });

        closeMenuItem.setOnAction(e -> Platform.exit());
    }

    @FXML
    public void about(ActionEvent actionEvent) {
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
        var zelfstandigNaamwoord = searchField.getText();
        var lidwoord = service.getLidwoord(zelfstandigNaamwoord);

        if (lidwoord.isPresent()) {
            Map<Field, String> container = service.getFields(lidwoord.get(), zelfstandigNaamwoord);
            setAllFields(container);
        } else {
            displayMessage("Woord niet gevonden...");
        }
    }

    private void setAllFields(Map<Field, String> container) {
        lidwoord.set(container.get(DE_OF_HET));
        aanwijzendVoornaamwoordVer.set(container.get(DIE_OF_DAT));
        aanwijzendVoornaamwoordDichtbij.set(container.get(DEZE_OF_DIT));
        bezittelijkVoornaamwoordOns.set(container.get(ONS_OF_ONZE));
        onbepaaldVoornaamwoord.set(container.get(ELK_OF_ELKE));
    }

    private void displayMessage(String message) {
        lidwoord.set(null);
        aanwijzendVoornaamwoordVer.set(null);
        aanwijzendVoornaamwoordDichtbij.set(message);
        bezittelijkVoornaamwoordOns.set(null);
        onbepaaldVoornaamwoord.set(null);
    }
}
