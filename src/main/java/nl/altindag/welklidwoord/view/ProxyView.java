package nl.altindag.welklidwoord.view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.converter.IntegerStringConverter;
import nl.altindag.welklidwoord.model.Proxy;
import nl.altindag.welklidwoord.service.ClientHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Optional;
import java.util.function.Function;

import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;
import static javafx.scene.control.ButtonType.CANCEL;
import static nl.altindag.welklidwoord.service.ClientHelper.HTTP_CLIENT_SUPPLIER;

@Component
public class ProxyView {

    private static final String SAVE_FILE = "proxydetails.wlw";
    private static final String HOME_DIRECTORY = System.getProperty("user.home");

    private Dialog<Optional<Proxy>> dialog;
    private Proxy proxy;
    private BooleanBinding fieldsAreEmptyBooleanBinding;
    private TextField username;
    private PasswordField password;
    private TextField host;
    private TextField port;
    private Button loginButton;
    private ButtonType loginButtonType;
    private ClientHelper clientHelper;

    @Autowired
    public ProxyView(ClientHelper clientHelper) {
        this.clientHelper = clientHelper;
        fieldsAreEmptyBooleanBinding = Bindings.createBooleanBinding(() -> false);

        Platform.runLater(() -> {
            initializeScreen();

            loadProxyDetailsFromFile().ifPresent(proxy -> {
                this.proxy = proxy;
                this.clientHelper.setClient(proxy);

                username.setText(proxy.getUsername());
                password.setText(proxy.getPassword());
                host.setText(proxy.getHost());
                port.setText(String.valueOf(proxy.getPort()));

            });

            fieldsAreEmptyBooleanBinding = host.textProperty()
                    .isEmpty()
                    .or(port.textProperty().isEmpty());

            loginButton.disableProperty().bind(fieldsAreEmptyBooleanBinding);
            dialog.setOnCloseRequest(event -> clientHelper.setClient(HTTP_CLIENT_SUPPLIER));
            dialog.setResultConverter(this::getProxyDetails);
        });
    }

    private void initializeScreen() {
        dialog = new Dialog<>();
        dialog.setTitle("Proxy Instellingen");
        dialog.setHeaderText("Pas hier uw netwerk instellingen aan");

        loginButtonType = new ButtonType("Login", OK_DONE);
        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(loginButtonType, CANCEL);

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

    private Optional<Proxy> getProxyDetails(ButtonType dialogButton) {
        Optional<Proxy> proxy = Optional.empty();
        if (dialogButton == loginButtonType) {
            if (!fieldsAreEmptyBooleanBinding.get()) {
                proxy = Optional.of(new Proxy()
                        .withUsername(username.getText())
                        .withPassword(password.getText())
                        .withHost(host.getText())
                        .withPort(Integer.valueOf(port.getText())));
            }
        }
        return proxy;
    }

    public void show() {
        dialog.showAndWait()
                .flatMap(Function.identity())
                .ifPresent(proxy -> {
                    this.proxy = proxy;
                    clientHelper.setClient(proxy);
                    saveProxyDetailsToFile(proxy);
                });
    }

    public BooleanBinding getFieldsAreEmptyBooleanBinding() {
        return fieldsAreEmptyBooleanBinding;
    }

    public Proxy getProxy() {
        return proxy;
    }

    private void saveProxyDetailsToFile(Proxy proxy) {
        try (FileOutputStream file = new FileOutputStream(HOME_DIRECTORY + "/" + SAVE_FILE); ObjectOutputStream out = new ObjectOutputStream(file)) {
            out.writeObject(proxy);
        } catch (IOException e) {
            // TODO
        }
    }

    private Optional<Proxy> loadProxyDetailsFromFile() {
        Optional<Proxy> proxy;
        try (FileInputStream file = new FileInputStream(System.getProperty("user.home") + "/" + SAVE_FILE); ObjectInputStream in = new ObjectInputStream(file)) {
            proxy = Optional.of((Proxy) in.readObject());
        } catch (IOException | ClassNotFoundException e) {
            proxy = Optional.empty();
        }
        return proxy;
    }

}
