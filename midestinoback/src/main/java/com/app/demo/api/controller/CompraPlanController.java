package com.app.demo.api.controller;

import com.app.demo.domain.dto.CompraPlanDTO;
import com.app.demo.domain.response.CompraPlanResponse;
import com.app.demo.domain.service.CompraPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compras")
public class CompraPlanController {

    @Autowired
    private CompraPlanService compraPlanService;

    @PostMapping("/agregar")
    public ResponseEntity<CompraPlanResponse> agregarCompraPlan(@RequestBody CompraPlanDTO compraPlanDTO) {
        CompraPlanResponse response = compraPlanService.agregarCompraPlan(compraPlanDTO);

        if (response.isValid()) {
            return ResponseEntity.status(201).body(response); // Código 201 para creación exitosa
        } else {
            return ResponseEntity.status(400).body(response); // Código 400 para error de entrada
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<CompraPlanResponse> obtenerComprasPorCliente(@PathVariable Long clienteId) {
        CompraPlanResponse response = compraPlanService.obtenerComprasPorCliente(clienteId);
        if (response.isValid()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body(response);
        }
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<CompraPlanResponse> obtenerComprasPorEmpresa(@PathVariable Long empresaId) {
        CompraPlanResponse response = compraPlanService.obtenerComprasPorEmpresa(empresaId);

        if (response.isValid()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body(response);
        }
    }

}
