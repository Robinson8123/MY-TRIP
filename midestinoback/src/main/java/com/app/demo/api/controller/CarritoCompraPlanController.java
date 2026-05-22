package com.app.demo.api.controller;

import com.app.demo.domain.dto.CarritoCompraPlanDTO;
import com.app.demo.domain.exception.PlanAlreadyInCarritoException;
import com.app.demo.domain.response.CarritoCompraPlanReponse;
import com.app.demo.domain.service.CarritoCompraPlanService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carritos")
public class CarritoCompraPlanController {

    @Autowired
    private CarritoCompraPlanService carritoService;

    @PostMapping("/guardar")
    public ResponseEntity<CarritoCompraPlanReponse> guardarCarrito(@RequestBody CarritoCompraPlanDTO carritoDTO) {
        try {
            CarritoCompraPlanReponse response = carritoService.guardarCarrito(carritoDTO);
            if (!response.isValid()) {
                return ResponseEntity.badRequest().body(response);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (PlanAlreadyInCarritoException ex) {
            CarritoCompraPlanReponse response = CarritoCompraPlanReponse.builder()
                    .message(ex.getMessage())
                    .valid(false)
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<CarritoCompraPlanReponse> obtenerCarritos() {
        CarritoCompraPlanReponse response = carritoService.obtenerCarritos();
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/usuario/{clienteId}", "/cliente/{clienteId}"})
    public ResponseEntity<CarritoCompraPlanReponse> obtenerCarritosPorCliente(@PathVariable Long clienteId) {
        CarritoCompraPlanReponse response = carritoService.obtenerCarritosPorCliente(clienteId);
        if (!response.isValid()) {
            return ResponseEntity.badRequest().body(response);
        }

        if (response.getCarritosList() == null || response.getCarritosList().isEmpty()) {
            CarritoCompraPlanReponse emptyResponse = CarritoCompraPlanReponse.builder()
                    .message(response.getMessage())
                    .valid(true)
                    .carritosList(List.of())
                    .build();
            return ResponseEntity.ok(emptyResponse);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<CarritoCompraPlanReponse> eliminarProducto(@PathVariable Long id) {
        CarritoCompraPlanReponse response = carritoService.eliminarCompra(id);
        return ResponseEntity.ok(response);
    }
}
