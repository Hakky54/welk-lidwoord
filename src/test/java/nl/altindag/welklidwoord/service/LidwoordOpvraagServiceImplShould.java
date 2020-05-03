package nl.altindag.welklidwoord.service;

import nl.altindag.welklidwoord.model.Field;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static nl.altindag.welklidwoord.model.Field.*;
import static nl.altindag.welklidwoord.model.Lidwoord.DE;
import static nl.altindag.welklidwoord.model.Lidwoord.HET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LidwoordOpvraagServiceImplShould {

    @Mock
    private VanDaleService vanDaleService;
    @Mock
    private WelkLidwoordService welkLidwoordService;
    @Mock
    private WoordenService woordenService;
    @InjectMocks
    private LidwoordOpvraagServiceImpl victim;

    @Test
    public void getLidwoord() {
        when(welkLidwoordService.getLidwoord(anyString())).thenReturn(CompletableFuture.completedFuture(DE));

        var response = victim.getLidwoord("boom");
        assertThat(response.join()).isEqualTo(DE);
    }

    @Test
    public void getLidwoordWhenWelkLidwoordServiceIsUnavailable() {
        when(welkLidwoordService.getLidwoord(anyString())).thenReturn(CompletableFuture.failedFuture(new RuntimeException("SERVICE NOT AVAILABLE")));
        when(woordenService.getLidwoord(anyString())).thenReturn(CompletableFuture.completedFuture(DE));

        var response = victim.getLidwoord("boom");
        assertThat(response.join()).isEqualTo(DE);
    }

    @Test
    public void getLidwoordWhenWelkLidwoordServiceAndWoordenServiceAreUnavailable() {
        when(welkLidwoordService.getLidwoord(anyString())).thenReturn(CompletableFuture.failedFuture(new RuntimeException("SERVICE NOT AVAILABLE")));
        when(woordenService.getLidwoord(anyString())).thenReturn(CompletableFuture.failedFuture(new RuntimeException("SERVICE NOT AVAILABLE")));
        when(vanDaleService.getLidwoord(anyString())).thenReturn(CompletableFuture.completedFuture(DE));

        var response = victim.getLidwoord("boom");
        assertThat(response.join()).isEqualTo(DE);
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
