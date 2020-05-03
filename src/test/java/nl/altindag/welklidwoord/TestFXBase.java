package nl.altindag.welklidwoord;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.concurrent.TimeoutException;

public class TestFXBase extends ApplicationTest {

    @BeforeAll
    public static void setUpHeadLessMode() {
        if (Boolean.parseBoolean(System.getenv("headless"))) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }
    }

    @BeforeEach
    public void setUpClass() throws Exception {
        ApplicationTest.launch(App.class);
    }

    @Override
    public void start(Stage stage) {
        stage.show();
    }

    @AfterEach
    public void afterEachTest() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    public  <T extends Node> T find(final String query) {
        return (T) lookup(query).query();
    }

}
