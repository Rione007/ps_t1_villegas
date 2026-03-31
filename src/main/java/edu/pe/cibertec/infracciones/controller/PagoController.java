package edu.pe.cibertec.infracciones.controller;

import edu.pe.cibertec.infracciones.dto.PagoResponseDTO;
import edu.pe.cibertec.infracciones.service.IPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final IPagoService pagoService;

    @PostMapping("/{multaId}")
    public ResponseEntity<Void> procesarPago(@PathVariable Long multaId) {
        pagoService.procesarPago(multaId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/infractor/{infractorId}")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPorInfractor(@PathVariable Long infractorId) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorInfractor(infractorId));
    }
}