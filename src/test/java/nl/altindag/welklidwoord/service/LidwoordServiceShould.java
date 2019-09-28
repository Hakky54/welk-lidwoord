package nl.altindag.welklidwoord.service;

import static nl.altindag.welklidwoord.model.Field.DEZE_OF_DIT;
import static nl.altindag.welklidwoord.model.Field.DE_OF_HET;
import static nl.altindag.welklidwoord.model.Field.DIE_OF_DAT;
import static nl.altindag.welklidwoord.model.Field.ELK_OF_ELKE;
import static nl.altindag.welklidwoord.model.Field.ONS_OF_ONZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Alert;
import nl.altindag.welklidwoord.FXTestUtils;
import nl.altindag.welklidwoord.model.Lidwoord;

@RunWith(MockitoJUnitRunner.class)
public class LidwoordServiceShould {

    @InjectMocks
    private LidwoordService lidwoordService;
    @Mock
    private LidwoordOpvraagService lidwoordOpvraagService;

    @BeforeClass
    public static void beforeClassSetUp() {
        JFXPanel jfxPanel = new JFXPanel();
    }

    @Before
    public void setUp() throws InterruptedException {
        FXTestUtils.waitForRunLater();
    }

    @Test
    public void getAboutScreen() {
        Alert aboutScreen = lidwoordService.getAboutScreen();

        assertThat(aboutScreen.getTitle()).isEqualTo("Over Welk Lidwoord");
        assertThat(aboutScreen.getHeaderText()).isEqualTo("Over Welk Lidwoord");
        assertThat(aboutScreen.getContentText()).isEqualTo("App versie 1.0\nGemaakt door Hakan Altındağ");
        assertThat(aboutScreen.getAlertType()).isEqualTo(Alert.AlertType.INFORMATION);
    }

    @Test
    public void returnAllFieldsAfterSearching() throws InterruptedException {
        when(lidwoordOpvraagService.getLidwoord(anyString()))
               .thenReturn(CompletableFuture.completedFuture(Lidwoord.DE));
        when(lidwoordOpvraagService.getFields(any(Lidwoord.class), anyString()))
                .thenReturn(Map.of(DE_OF_HET, "de fiets",
                                   DIE_OF_DAT, "die fiets",
                                   DEZE_OF_DIT, "deze fiets",
                                   ELK_OF_ELKE, "elke fiets",
                                   ONS_OF_ONZE, "onze fiets"));

        lidwoordService.search("fiets");
        FXTestUtils.waitForRunLater();

        assertThat(lidwoordService.lidwoordProperty().get()).isEqualTo("de fiets");
        assertThat(lidwoordService.aanwijzendVoornaamwoordVerProperty().get()).isEqualTo("die fiets");
        assertThat(lidwoordService.aanwijzendVoornaamwoordDichtbijProperty().get()).isEqualTo("deze fiets");
        assertThat(lidwoordService.onbepaaldVoornaamwoordProperty().get()).isEqualTo("elke fiets");
        assertThat(lidwoordService.bezittelijkVoornaamwoordOnsProperty().get()).isEqualTo("onze fiets");
    }

    @Test
    public void provideDefaultValuesWhenTheUserHasNotSearchedYet() throws InterruptedException {
        assertThat(lidwoordService.lidwoordProperty().get()).isEqualTo("de of het");
        assertThat(lidwoordService.aanwijzendVoornaamwoordVerProperty().get()).isEqualTo("die of dat");
        assertThat(lidwoordService.aanwijzendVoornaamwoordDichtbijProperty().get()).isEqualTo("deze of dit");
        assertThat(lidwoordService.onbepaaldVoornaamwoordProperty().get()).isEqualTo("elk of elke");
        assertThat(lidwoordService.bezittelijkVoornaamwoordOnsProperty().get()).isEqualTo("ons of onze");
    }

}
