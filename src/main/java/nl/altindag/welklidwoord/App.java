package nl.altindag.welklidwoord;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.altindag.welklidwoord.presentation.search.SearchView;

public class App extends Application {

    private static final String TITLE = "Welk lidwoord?";
    private static final String CSS = App.class.getResource("app.css").toExternalForm();

    @Override
    public void start(Stage stage) throws Exception {
        SearchView searchView = new SearchView();

        Scene scene = new Scene(searchView.getView());
        stage.setTitle(TITLE);
        scene.getStylesheets().add(CSS);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
