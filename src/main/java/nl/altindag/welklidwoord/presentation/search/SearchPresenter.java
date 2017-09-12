package nl.altindag.welklidwoord.presentation.search;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import nl.altindag.welklidwoord.exception.WLException;
import nl.altindag.welklidwoord.presentation.proxy.ProxyPresenter;
import nl.altindag.welklidwoord.service.LidWoordService;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.ResourceBundle;

import static nl.altindag.welklidwoord.service.LidWoordService.*;

public class SearchPresenter implements Initializable {

    @FXML
    private Label aanwijzendLabel;
    @FXML
    private Label bezittelijkJouLabel;
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

    @Inject
    private LidWoordService service;
    @Inject
    private ProxyPresenter proxyPresenter;

    private SimpleStringProperty lidwoord = new SimpleStringProperty("de of het");
    private SimpleStringProperty aanwijzendVoornaamwoord = new SimpleStringProperty("die of dat");
    private SimpleStringProperty bezittelijkVoornaamwoordOns = new SimpleStringProperty("ons of onze");
    private SimpleStringProperty onbepaaldVoornaamwoord = new SimpleStringProperty("elk of elke");
    private SimpleStringProperty bezittelijkVoornaamwoordJou = new SimpleStringProperty("jou of jouw");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lidwoordLabel.textProperty().bind(lidwoord);
        onbepaaldLabel.textProperty().bind(onbepaaldVoornaamwoord);
        bezittelijkJouLabel.textProperty().bind(bezittelijkVoornaamwoordJou);
        bezittelijkOnsLabel.textProperty().bind(bezittelijkVoornaamwoordOns);
        aanwijzendLabel.textProperty().bind(aanwijzendVoornaamwoord);

        proxySettingsMenuItem.setOnAction(e -> proxyPresenter.show());
        proxyCheckMenuItem.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                service.setDefaultHttpClient();
            } else {
                proxyPresenter.getResult().ifPresent(service::setProxy);
            }
        });
        closeMenuItem.setOnAction(e -> Platform.exit());
        proxyCheckMenuItem.disableProperty().bind(proxyPresenter.getFieldsAreEmptyBooleanBinding());
    }

    @FXML
    public void onEnter(ActionEvent event) {
        Map<String, String> container = null;
        try {
            container = service.get(searchField.getText());
        } catch (WLException e) {
            displayException(e.getMessage());
            return;
        } catch (UnknownHostException e) {
            displayException("Onjuiste proxy");
            return;
        } catch (IOException e) {
            displayException("Onbekende fout");
        }

        lidwoord.set(container.get(DE_OF_HET));
        aanwijzendVoornaamwoord.set(container.get(DIE_OF_DAT).substring(container.get(DIE_OF_DAT).indexOf(":") + 2));
        bezittelijkVoornaamwoordOns.set(container.get(ONS_OF_ONZE).substring(container.get(ONS_OF_ONZE).indexOf(":") + 2));
        bezittelijkVoornaamwoordJou.set(container.get(JOU_OF_JOUW));
        onbepaaldVoornaamwoord.set(container.get(ELK_OF_ELKE).substring(container.get(ELK_OF_ELKE).indexOf(":") + 2));
    }

    private void displayException(String exception) {
        lidwoord.set(null);
        aanwijzendVoornaamwoord.set(null);
        bezittelijkVoornaamwoordJou.set(exception);
        bezittelijkVoornaamwoordOns.set(null);
        onbepaaldVoornaamwoord.set(null);
    }
}
