package nl.altindag.welklidwoord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.testfx.api.FxRobotContext;

import javafx.application.Platform;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class FXTestUtils {

    public static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(semaphore::release);
        semaphore.acquire();
    }

    // Got this from https://stackoverflow.com/questions/48565782/testfx-how-to-test-validation-dialogs-with-no-ids
    public static Stage getTopModalStage(FxRobotContext fxRobotContext) {
        final List<Window> allWindows = new ArrayList<>(fxRobotContext.getWindowFinder().listWindows());
        Collections.reverse(allWindows);

        return (Stage) allWindows
                .stream()
                .filter(window -> window instanceof Stage)
                .filter(window -> ((Stage) window).getModality() == Modality.APPLICATION_MODAL)
                .findFirst()
                .orElse(null);
    }

}
