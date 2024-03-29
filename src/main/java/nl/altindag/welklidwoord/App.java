/*
 * Copyright 2017 Thunderberry.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.altindag.welklidwoord;

import com.guigarage.flatterfx.FlatterFX;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.altindag.ssl.SSLFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.function.Function;

import static javafx.stage.StageStyle.UNDECORATED;

@SpringBootApplication
public class App extends Application {

    private static final String TITLE = "Welk lidwoord?";
    private ConfigurableApplicationContext applicationContext;
    private final Function<String, FXMLLoader> fxmlLoaderFunction = fxml -> new FXMLLoader(this.getClass().getResource(fxml));

    private Parent root;
    private Stage loadingStage;

    @Override
    public void init() throws IOException {
        Platform.runLater(this::displayLoadingScreen);
        applicationContext = new SpringApplicationBuilder(App.class)
                .headless(false)
                .run(getParameters().getRaw().stream().toArray(String[]::new));

        FXMLLoader fxmlLoader = fxmlLoaderFunction.apply("/mainscreen.fxml");
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
        stage.setOnShowing(windowEvent -> loadingStage.close());

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
    public HttpClient httpClient(SSLFactory sslFactory) {
        return HttpClient.newBuilder()
                .sslContext(sslFactory.getSslContext())
                .sslParameters(sslFactory.getSslContext().getDefaultSSLParameters())
                .build();
    }

    @Bean
    public SSLFactory sslFactory() {
        return SSLFactory.builder()
                .withUnsafeTrustMaterial()
                .build();
    }

    private void displayLoadingScreen() {
        FXMLLoader fxmlLoader = fxmlLoaderFunction.apply("/loadingscreen.fxml");
        Parent root;

        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(root);
        loadingStage = new Stage();
        loadingStage.initStyle(UNDECORATED);
        loadingStage.setResizable(false);
        loadingStage.setAlwaysOnTop(true);
        loadingStage.setScene(scene);
        loadingStage.show();
    }

}
