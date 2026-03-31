package edu.pe.cibertec.infracciones;

import edu.pe.cibertec.infracciones.model.*;
import edu.pe.cibertec.infracciones.repository.MultaRepository;
import edu.pe.cibertec.infracciones.repository.PagoRepository;
import edu.pe.cibertec.infracciones.service.impl.PagoServiceImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PagoServiceImpl - Unit Test")
public class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private MultaRepository multaRepository;

    @InjectMocks
    private PagoServiceImpl pagoService;

    @Test
    @DisplayName("Debe aplicar recargo cuando multa está vencida")
    void debeAplicarRecargoCuandoMultaEstaVencida() {

        Multa multa = new Multa();
        multa.setId(1L);
        multa.setMonto(500.0);
        multa.setEstado(EstadoMulta.PENDIENTE);
        multa.setFechaEmision(LocalDate.now().minusDays(12));
        multa.setFechaVencimiento(LocalDate.now().minusDays(2));

        when(multaRepository.findById(1L))
                .thenReturn(Optional.of(multa));

        ArgumentCaptor<Pago> captor = ArgumentCaptor.forClass(Pago.class);

        pagoService.procesarPago(1L);

        verify(pagoRepository, times(1)).save(captor.capture());

        Pago pagoGuardado = captor.getValue();

        assertEquals(75.0, pagoGuardado.getRecargo());
        assertEquals(0.0, pagoGuardado.getDescuentoAplicado());
        assertEquals(575.0, pagoGuardado.getMontoPagado());
    }
}