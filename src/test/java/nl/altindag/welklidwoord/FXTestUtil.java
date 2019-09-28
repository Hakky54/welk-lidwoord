package nl.altindag.welklidwoord;

import java.util.concurrent.Semaphore;

import javafx.application.Platform;

public class FXTestUtil {

    public static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(semaphore::release);
        semaphore.acquire();
    }

}
