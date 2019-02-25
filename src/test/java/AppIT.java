import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import nl.altindag.welklidwoord.App;

public class AppIT extends ApplicationTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        ApplicationTest.launch(App.class);
    }

    @Test
    public void shouldReturnLidwoord() throws InterruptedException {
        Label lidwoordLabel = lookup("#lidwoordLabel").queryAs(Label.class);
        Label aanwijzendVerLabel = lookup("#aanwijzendVerLabel").queryAs(Label.class);
        Label aanwijzendDichtbijLabel = lookup("#aanwijzendDichtbijLabel").queryAs(Label.class);
        Label bezittelijkOnsLabel = lookup("#bezittelijkOnsLabel").queryAs(Label.class);
        Label onbepaaldLabel = lookup("#onbepaaldLabel").queryAs(Label.class);

        clickOn("#searchField").write("boom").type(KeyCode.ENTER);

        TimeUnit.MILLISECONDS.sleep(500);

        assertThat(lidwoordLabel.getText()).isEqualTo("de boom");
        assertThat(aanwijzendVerLabel.getText()).isEqualTo("die boom");
        assertThat(aanwijzendDichtbijLabel.getText()).isEqualTo("deze boom");
        assertThat(bezittelijkOnsLabel.getText()).isEqualTo("onze boom");
        assertThat(onbepaaldLabel.getText()).isEqualTo("elke boom");
    }

}
