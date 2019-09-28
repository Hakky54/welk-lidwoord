package nl.altindag.welklidwoord;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class AppIT extends TestFXBase {

    @Test
    public void shouldReturnLidwoord() throws InterruptedException {
        Label lidwoordLabel = find("#lidwoordLabel");
        Label aanwijzendVerLabel = find("#aanwijzendVerLabel");
        Label aanwijzendDichtbijLabel = find("#aanwijzendDichtbijLabel");
        Label bezittelijkOnsLabel = find("#bezittelijkOnsLabel");
        Label onbepaaldLabel = find("#onbepaaldLabel");

        clickOn("#searchField").write("boom").type(KeyCode.ENTER);

        TimeUnit.MILLISECONDS.sleep(500);

        assertThat(lidwoordLabel.getText()).isEqualTo("de boom");
        assertThat(aanwijzendVerLabel.getText()).isEqualTo("die boom");
        assertThat(aanwijzendDichtbijLabel.getText()).isEqualTo("deze boom");
        assertThat(bezittelijkOnsLabel.getText()).isEqualTo("onze boom");
        assertThat(onbepaaldLabel.getText()).isEqualTo("elke boom");
    }

    @Test
    public void shouldIgnoreSpaceWhenSearching() {
        TextField searchField = find("#searchField");

        clickOn("#searchField").write("fiets    ").type(KeyCode.ENTER);
        assertThat(searchField.getText()).isEqualTo("fiets");
    }

    @Test
    public void shouldReturnAboutScreen() {
        clickOn("#options").clickOn("#about");

        Stage actualAlertDialog = FXTestUtils.getTopModalStage(this.robotContext());
        assertThat(actualAlertDialog).isNotNull();

        DialogPane dialogPane = (DialogPane) actualAlertDialog.getScene().getRoot();
        assertThat(dialogPane.getHeaderText()).isEqualTo("Over Welk Lidwoord");
        assertThat(dialogPane.getContentText()).isEqualTo("App versie 1.0\nGemaakt door Hakan Altındağ");
    }

}
