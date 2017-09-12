package nl.altindag.welklidwoord;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.altindag.welklidwoord.presentation.search.SearchView;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SearchView searchView = new SearchView();

        Scene scene = new Scene(searchView.getView());
        stage.setTitle("Welk lidwoord?");
        final String uri = getClass().getResource("app.css").toExternalForm();
        scene.getStylesheets().add(uri);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
