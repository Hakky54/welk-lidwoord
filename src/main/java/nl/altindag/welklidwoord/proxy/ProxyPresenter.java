package nl.altindag.welklidwoord.proxy;

import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.converter.IntegerStringConverter;
import nl.altindag.welklidwoord.service.LidWoordService;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Optional;

import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;
import static javafx.scene.control.ButtonType.CANCEL;

public class ProxyPresenter {

    private Dialog<ProxyModel> dialog;
    private Optional<ProxyModel> result = Optional.empty();
    private BooleanBinding fieldsAreEmptyBooleanBinding;

    private TextField username;
    private PasswordField password;
    private TextField host;
    private TextField port;
    private Button loginButton;
    private ButtonType loginButtonType;

    @Inject
    private LidWoordService service;

    @PostConstruct
    public void init() {
        initializeScreen();

        fieldsAreEmptyBooleanBinding = username.textProperty().isEmpty()
                .or(password.textProperty().isEmpty())
                .or(host.textProperty().isEmpty())
                .or(port.textProperty().isEmpty());

        loginButton.disableProperty().bind(fieldsAreEmptyBooleanBinding);

        dialog.setOnCloseRequest(e -> service.setClient(HttpClientBuilder.create().build()));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                if (!fieldsAreEmptyBooleanBinding.get()) {
                    return new ProxyModel()
                            .withUsername(username.getText())
                            .withPassword(password.getText())
                            .withHost(host.getText())
                            .withPort(Integer.valueOf(port.getText()));
                } else {
                    return null;
                }
            }
            return null;
        });
    }

    private void initializeScreen() {
        dialog = new Dialog<>();
        dialog.setTitle("Proxy Instellingen");
        dialog.setHeaderText("Pas hier uw netwerk instellingen aan");

        loginButtonType = new ButtonType("Login", OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        username = new TextField();
        username.setPromptText("Gebruikersnaam");
        password = new PasswordField();
        password.setPromptText("Wachtwoord");

        host = new TextField();
        host.setPromptText("Host");
        port = new TextField();
        port.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        port.setPromptText("Port");

        grid.add(new Label("Credentials:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(password, 2, 0);
        grid.add(new Label("Proxy:"), 0, 1);
        grid.add(host, 1, 1);
        grid.add(port, 2, 1);

        loginButton = (Button) dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        dialog.getDialogPane().setContent(grid);
    }

    public void show() {
        result = dialog.showAndWait();
        result.ifPresent(service::setProxy);
    }

    public Optional<ProxyModel> getResult() {
        return result;
    }

    public BooleanBinding getFieldsAreEmptyBooleanBinding() {
        return fieldsAreEmptyBooleanBinding;
    }
}
