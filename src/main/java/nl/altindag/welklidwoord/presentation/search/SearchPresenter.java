package nl.altindag.welklidwoord.presentation.search;

import static nl.altindag.welklidwoord.service.AbstractService.HTTP_CLIENT_SUPPLIER;

import java.net.URL;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javax.inject.Inject;
import nl.altindag.welklidwoord.exception.WLException;
import nl.altindag.welklidwoord.model.Field;
import nl.altindag.welklidwoord.presentation.proxy.ProxyPresenter;
import nl.altindag.welklidwoord.service.VanDaleService;

public class SearchPresenter implements Initializable {

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

    @Inject
    private VanDaleService service;
    @Inject
    private ProxyPresenter proxyPresenter;

    private SimpleStringProperty lidwoord = new SimpleStringProperty("de of het");
    private SimpleStringProperty aanwijzendVoornaamwoordVer = new SimpleStringProperty("die of dat");
    private SimpleStringProperty aanwijzendVoornaamwoordDichtbij = new SimpleStringProperty("deze of dit");
    private SimpleStringProperty bezittelijkVoornaamwoordOns = new SimpleStringProperty("ons of onze");
    private SimpleStringProperty onbepaaldVoornaamwoord = new SimpleStringProperty("elk of elke");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lidwoordLabel.textProperty().bind(lidwoord);
        onbepaaldLabel.textProperty().bind(onbepaaldVoornaamwoord);
        aanwijzendDichtbijLabel.textProperty().bind(aanwijzendVoornaamwoordDichtbij);
        bezittelijkOnsLabel.textProperty().bind(bezittelijkVoornaamwoordOns);
        aanwijzendVerLabel.textProperty().bind(aanwijzendVoornaamwoordVer);

        proxySettingsMenuItem.setOnAction(e -> proxyPresenter.show());
        proxyCheckMenuItem.selectedProperty()
                          .addListener((observable, oldValue, newValue) -> {
                              if (newValue) {
                                  service.setClient(HTTP_CLIENT_SUPPLIER);
                              } else {
                                  service.setClient(proxyPresenter.getProxy());
                              }
                          });
        closeMenuItem.setOnAction(e -> Platform.exit());
        proxyCheckMenuItem.disableProperty()
                          .bind(proxyPresenter.getFieldsAreEmptyBooleanBinding());
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
            displayException("Onjuiste proxy -.-'");
            return;
        } catch (Exception e) {
            displayException("Onbekende fout (0|0)");
            return;
        }

        lidwoord.set(container.get(Field.DE_OF_HET));
        aanwijzendVoornaamwoordVer.set(container.get(Field.DIE_OF_DAT));
        aanwijzendVoornaamwoordDichtbij.set(container.get(Field.DEZE_OF_DIT));
        bezittelijkVoornaamwoordOns.set(container.get(Field.ONS_OF_ONZE));
        onbepaaldVoornaamwoord.set(container.get(Field.ELK_OF_ELKE));
    }

    private void displayException(String exception) {
        lidwoord.set(null);
        aanwijzendVoornaamwoordVer.set(null);
        aanwijzendVoornaamwoordDichtbij.set(exception);
        bezittelijkVoornaamwoordOns.set(null);
        onbepaaldVoornaamwoord.set(null);
    }
}
