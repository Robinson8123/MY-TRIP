package com.app.demo.api.controller;

import com.app.demo.domain.dto.RecomendacionDTO;
import com.app.demo.domain.service.RecomendacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recomendaciones")
public class RecomendacionController {

    @Autowired
    private RecomendacionService recomendacionService;

    @GetMapping("/{clienteId}")
    public ResponseEntity<List<RecomendacionDTO>> obtenerRecomendaciones(
            @PathVariable Long clienteId) {
        return ResponseEntity.ok(recomendacionService.recomendar(clienteId));
    }
}
