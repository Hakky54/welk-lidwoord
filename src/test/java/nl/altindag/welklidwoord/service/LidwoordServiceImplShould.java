package nl.altindag.welklidwoord.service;

import static nl.altindag.welklidwoord.model.Field.DEZE_OF_DIT;
import static nl.altindag.welklidwoord.model.Field.DE_OF_HET;
import static nl.altindag.welklidwoord.model.Field.DIE_OF_DAT;
import static nl.altindag.welklidwoord.model.Field.ELK_OF_ELKE;
import static nl.altindag.welklidwoord.model.Field.ONS_OF_ONZE;
import static nl.altindag.welklidwoord.model.Lidwoord.DE;
import static nl.altindag.welklidwoord.model.Lidwoord.HET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import nl.altindag.welklidwoord.model.Field;
import nl.altindag.welklidwoord.model.Lidwoord;

@RunWith(MockitoJUnitRunner.class)
public class LidwoordServiceImplShould {

    @Mock
    private VanDaleService vanDaleService;
    @Mock
    private WelkLidwoordService welkLidwoordService;
    @Mock
    private WoordenService woordenService;
    @InjectMocks
    private LidwoordServiceImpl victim;

    @Test
    public void getLidwoord() throws ExecutionException, InterruptedException {
        when(welkLidwoordService.getLidwoord(anyString())).thenReturn(CompletableFuture.completedFuture(DE));

        CompletableFuture<Lidwoord> response = victim.getLidwoord("boom");
        assertThat(response.get()).isEqualTo(DE);
    }

    @Test
    public void getLidwoordWhenWelkLidwoordServiceIsUnavailable() throws ExecutionException, InterruptedException {
        when(welkLidwoordService.getLidwoord(anyString())).thenReturn(CompletableFuture.failedFuture(new RuntimeException("SERVICE NOT AVAILABLE")));
        when(woordenService.getLidwoord(anyString())).thenReturn(CompletableFuture.completedFuture(DE));

        CompletableFuture<Lidwoord> response = victim.getLidwoord("boom");
        assertThat(response.get()).isEqualTo(DE);
    }

    @Test
    public void getLidwoordWhenWelkLidwoordServiceAndWoordenServiceAreUnavailable() throws ExecutionException, InterruptedException {
        when(welkLidwoordService.getLidwoord(anyString())).thenReturn(CompletableFuture.failedFuture(new RuntimeException("SERVICE NOT AVAILABLE")));
        when(woordenService.getLidwoord(anyString())).thenReturn(CompletableFuture.failedFuture(new RuntimeException("SERVICE NOT AVAILABLE")));
        when(vanDaleService.getLidwoord(anyString())).thenReturn(CompletableFuture.completedFuture(DE));

        CompletableFuture<Lidwoord> response = victim.getLidwoord("boom");
        assertThat(response.get()).isEqualTo(DE);
    }

    @Test
    public void getFieldsForDeLidwoord() {
        Map<Field, String> fields = victim.getFields(DE, "boom");
        assertThat(fields.get(DE_OF_HET)).isEqualTo("de boom");
        assertThat(fields.get(DEZE_OF_DIT)).isEqualTo("deze boom");
        assertThat(fields.get(DIE_OF_DAT)).isEqualTo("die boom");
        assertThat(fields.get(ELK_OF_ELKE)).isEqualTo("elke boom");
        assertThat(fields.get(ONS_OF_ONZE)).isEqualTo("onze boom");
    }

    @Test
    public void getFieldsForHetLidwoord() {
        Map<Field, String> fields = victim.getFields(HET, "huis");
        assertThat(fields.get(DE_OF_HET)).isEqualTo("het huis");
        assertThat(fields.get(DEZE_OF_DIT)).isEqualTo("dit huis");
        assertThat(fields.get(DIE_OF_DAT)).isEqualTo("dat huis");
        assertThat(fields.get(ELK_OF_ELKE)).isEqualTo("elk huis");
        assertThat(fields.get(ONS_OF_ONZE)).isEqualTo("ons huis");
    }

    @Test
    public void getContainer() {
        Map<Field, String> container = victim.getContainer();
        assertThat(container.size()).isEqualTo(5);
    }

}
