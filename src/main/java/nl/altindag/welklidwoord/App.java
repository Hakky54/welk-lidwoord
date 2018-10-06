package nl.altindag.welklidwoord;

import com.guigarage.flatterfx.FlatterFX;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.http.HttpClient;

@SpringBootApplication
public class App extends Application {

    private ConfigurableApplicationContext applicationContext;
    private Parent root;
    private static final String TITLE = "Welk lidwoord?";

    @Override
    public void init() throws IOException {
        applicationContext = SpringApplication.run(App.class);

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/mainscreen.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        root = fxmlLoader.load();
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(root);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.show();
        FlatterFX.style();
    }

    @Override
    public void stop() {
        Platform.exit();
        applicationContext.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }

}
