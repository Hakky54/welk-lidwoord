package nl.altindag.welklidwoord.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import nl.altindag.welklidwoord.service.LidwoordService;
import org.springframework.stereotype.Controller;

import static javafx.geometry.Pos.CENTER;
import static org.apache.commons.lang3.StringUtils.SPACE;

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
    private final LidwoordService lidwoordService;

    public LidwoordController(LidwoordService lidwoordService) {
        this.lidwoordService = lidwoordService;
    }

    @FXML
    public void initialize() {
        lidwoordLabel.textProperty().bind(lidwoordService.lidwoordProperty());
        onbepaaldLabel.textProperty().bind(lidwoordService.onbepaaldVoornaamwoordProperty());
        aanwijzendDichtbijLabel.textProperty().bind(lidwoordService.aanwijzendVoornaamwoordDichtbijProperty());
        bezittelijkOnsLabel.textProperty().bind(lidwoordService.bezittelijkVoornaamwoordOnsProperty());
        aanwijzendVerLabel.textProperty().bind(lidwoordService.aanwijzendVoornaamwoordVerProperty());

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
        lidwoordService.getAboutScreen().show();
    }

    @FXML
    public void onEnter(ActionEvent event) {
        String zelfstandigNaamwoord = searchField.getText().toLowerCase();
        lidwoordService.search(zelfstandigNaamwoord);
    }

}
