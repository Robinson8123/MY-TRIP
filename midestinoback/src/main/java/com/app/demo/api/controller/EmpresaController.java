package com.app.demo.api.controller;

import com.app.demo.domain.dto.EmpresaDTO;
import com.app.demo.domain.response.EmpresaResponse;
import com.app.demo.domain.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @PostMapping("/iniciar-sesion")
    public ResponseEntity<EmpresaResponse> iniciarSesion(@RequestBody EmpresaDTO empresaDTO) {
        EmpresaResponse response = empresaService.iniciarSesion(empresaDTO);

        if (response.isValid()) {
            return ResponseEntity.ok(response);
        } else {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/agregar")
    public ResponseEntity<EmpresaResponse> agregarEmpresa(@RequestBody EmpresaDTO empresaDTO) {
        EmpresaResponse response = new EmpresaResponse();

        try {
            EmpresaDTO savedEmpresa = empresaService.agregarEmpresa(empresaDTO);
            response.setMessage("Empresa agregada correctamente");
            response.setValid(true);
            response.setEmpresa(savedEmpresa);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setValid(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/foto-perfil/{idEmpresa}")
    public ResponseEntity<EmpresaResponse> agregarFotoPerfilCliente(
            @PathVariable Long idEmpresa,
            @RequestPart("imagen") MultipartFile imagen) {
        EmpresaResponse response = new EmpresaResponse();
        try {

            response = empresaService.agregarImagenPerfil(idEmpresa, imagen);

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setValid(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/imagen")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable Long id) {
        try {
            byte[] imagenContenido = empresaService.obtenerImagenPerfil(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/png")
                    .body(imagenContenido);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EmpresaResponse> actualizarEmpresa(@PathVariable Long id, @RequestBody EmpresaDTO empresaDTO) {
        EmpresaResponse response = new EmpresaResponse();

        try {
            EmpresaDTO updatedEmpresa = empresaService.actualizarEmpresa(id, empresaDTO);
            response.setMessage("Empresa actualizada correctamente");
            response.setValid(true);
            response.setEmpresa(updatedEmpresa);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setMessage("Error al actualizar empresa: " + e.getMessage());
            response.setValid(false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/todas")
    public ResponseEntity<List<EmpresaDTO>> obtenerTodasLasEmpresas() {
        List<EmpresaDTO> empresas = empresaService.obtenerTodasLasEmpresas();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDTO> obtenerEmpresaPorId(@PathVariable Long id) {
        try {
            EmpresaDTO empresa = empresaService.obtenerEmpresaPorId(id);
            return ResponseEntity.ok(empresa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
