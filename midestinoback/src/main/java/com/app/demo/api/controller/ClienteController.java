package com.app.demo.api.controller;

import com.app.demo.domain.dto.ClienteDTO;
import com.app.demo.domain.response.ClienteResponse;
import com.app.demo.domain.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/iniciar-sesion")
    public ResponseEntity<ClienteResponse> iniciarSesion(@RequestBody ClienteDTO clienteDTO) {
        ClienteResponse response = clienteService.iniciarSesion(clienteDTO);

        if (response.isValid()) {
            return ResponseEntity.ok(response);
        } else {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/agregar")
    public ResponseEntity<ClienteResponse> agregarCliente(@RequestBody ClienteDTO clienteDTO) {
        ClienteResponse response = new ClienteResponse();

        try {
            ClienteDTO savedCliente = clienteService.agregarCliente(clienteDTO);
            response.setMessage("Cliente agregado correctamente");
            response.setValid(true);
            response.setCliente(savedCliente);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setValid(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/foto-perfil/{idCliente}")
    public ResponseEntity<ClienteResponse> agregarFotoPerfilCliente(
            @PathVariable Long idCliente,
            @RequestPart("imagen") MultipartFile imagen) {
        ClienteResponse response = new ClienteResponse();
        try {

            response = clienteService.agregarImagenPerfil(idCliente, imagen);

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
            byte[] imagenContenido = clienteService.obtenerImagenPerfil(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/png") // Ajusta el tipo de contenido si es necesario
                    .body(imagenContenido);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ClienteResponse> actualizarCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO) {
        ClienteResponse response = new ClienteResponse();

        try {
            ClienteDTO updatedCliente = clienteService.actualizarCliente(id, clienteDTO);
            response.setMessage("Cliente actualizado correctamente");
            response.setValid(true);
            response.setCliente(updatedCliente);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setMessage("Error al actualizar cliente: " + e.getMessage());
            response.setValid(false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/todos")
    public ResponseEntity<List<ClienteDTO>> obtenerTodosLosClientes() {
        List<ClienteDTO> clientes = clienteService.obtenerTodasLasEmpresas();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerClientePorId(@PathVariable Long id) {
        try {
            ClienteDTO cliente = clienteService.obtenerClientePorId(id);
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
