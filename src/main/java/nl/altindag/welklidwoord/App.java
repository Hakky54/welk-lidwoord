package nl.altindag.welklidwoord;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nl.altindag.welklidwoord.presentation.search.SearchView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App extends Application {

    private static final String TITLE = "Welk lidwoord?";
    private static final String CSS = App.class.getResource("app.css").toExternalForm();

    public static final ExecutorService executor = Executors.newFixedThreadPool(10);

    @Override
    public void start(Stage stage) {
        SearchView searchView = new SearchView();

        Scene scene = new Scene(searchView.getView());
        stage.setTitle(TITLE);
        scene.getStylesheets().add(CSS);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(App::shutDown);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static void shutDown(WindowEvent event) {
        executor.shutdownNow();
        Platform.exit();
    }
}
