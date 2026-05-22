package com.app.demo.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.demo.domain.dto.PlanEmpresaDTO;
import com.app.demo.domain.response.PlanEmpresaResponse;
import com.app.demo.domain.service.PlanEmpresaService;

@RestController
@RequestMapping("/api/planes")
public class PlanEmpresaController {

    @Autowired
    private PlanEmpresaService planEmpresaService;

    @PostMapping("/agregar")
    public PlanEmpresaResponse agregar(
            @RequestPart("plan") PlanEmpresaDTO dto,
            @RequestPart("imagen") MultipartFile imagen) {
        return planEmpresaService.agregar(dto, imagen);
    }

    @PostMapping("/{planId}/valoracion")
    public ResponseEntity<String> agregarValoracion(
            @PathVariable Long planId,
            @RequestParam Long clienteId,
            @RequestParam Integer puntuacion,
            @RequestParam(required = false) String comentario) {
        try {
            planEmpresaService.agregarValoracion(planId, clienteId, puntuacion, comentario);
            return ResponseEntity.ok("Valoración añadida exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al agregar valoración: " + e.getMessage());
        }
    }

    @GetMapping("/{planId}/resenas")
    public ResponseEntity<?> obtenerResenas(@PathVariable Long planId) {
        try {
            return ResponseEntity.ok(planEmpresaService.obtenerValoracionesPorPlan(planId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener reseñas: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/imagen")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable Long id) {
        try {
            byte[] imagenContenido = planEmpresaService.obtenerImagen(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/png") // Ajusta el tipo de contenido si es necesario
                    .body(imagenContenido);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public PlanEmpresaResponse eliminar(@PathVariable Long id) {
        return planEmpresaService.eliminar(id);
    }

    @PutMapping("/actualizar")
    public PlanEmpresaResponse actualizar(@RequestBody PlanEmpresaDTO dto) {
        return planEmpresaService.actualizar(dto);
    }

    @GetMapping("/todos")
    public PlanEmpresaResponse obtenerTodos() {
        return planEmpresaService.obtenerTodos();
    }

    @GetMapping("/empresa/{empresaId}")
    public PlanEmpresaResponse obtenerPorEmpresa(@PathVariable Long empresaId) {
        return planEmpresaService.obtenerPorEmpresa(empresaId);
    }
}
