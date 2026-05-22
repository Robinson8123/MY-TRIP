package com.app.demo.domain.service;

import com.app.demo.domain.dto.ClienteDTO;
import com.app.demo.domain.mapper.ClienteMapper;
import com.app.demo.domain.response.ClienteResponse;
import com.app.demo.persistence.entity.Cliente;
import com.app.demo.persistence.entity.Imagen;
import com.app.demo.persistence.repository.ClienteRepository;
import com.app.demo.persistence.repository.ImagenRepository;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ImagenRepository imagenRepository;

    public ClienteResponse iniciarSesion(ClienteDTO clienteDTO) {
        ClienteResponse response = new ClienteResponse();

        try {
            Optional<Cliente> optionalCliente = clienteRepository.findByEmailAndContrasena(clienteDTO.getEmail(), clienteDTO.getContrasena());

            if (optionalCliente.isPresent()) {
                Cliente cliente = optionalCliente.get();
                response.setMessage("Inicio de sesión exitoso");
                response.setValid(true);
                response.setCliente(ClienteMapper.toDTO(cliente)); // Convierte a DTO si es necesario
            } else {
                response.setMessage("Credenciales incorrectas.");
                response.setValid(false);
            }
        } catch (Exception e) {
            response.setMessage("Error al iniciar sesión: " + e.getMessage());
            response.setValid(false);
        }

        return response;
    }

    public ClienteDTO agregarCliente(ClienteDTO clienteDTO) throws Exception {
        try {
            // Verificar si ya existe un cliente con el mismo número de documento, teléfono, email o nombre de usuario
            if (clienteRepository.existsByNumeroDocumento(clienteDTO.getNumeroDocumento())) {
                throw new Exception("El número de documento ya está registrado.");
            }
            if (clienteRepository.existsByNumeroTelefono(clienteDTO.getNumeroTelefono())) {
                throw new Exception("El número de teléfono ya está registrado.");
            }
            if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
                throw new Exception("El correo electrónico ya está registrado.");
            }
            if (clienteRepository.existsByNombreUsuario(clienteDTO.getNombreUsuario())) {
                throw new Exception("El nombre de usuario ya está registrado.");
            }

            // Si no existen, se guarda el nuevo cliente
            Cliente cliente = ClienteMapper.toEntity(clienteDTO);
            cliente = clienteRepository.save(cliente);
            return ClienteMapper.toDTO(cliente);
        } catch (Exception e) {
            throw new Exception("Error al agregar cliente: " + e.getMessage(), e);
        }
    }

    public ClienteResponse agregarImagenPerfil(Long clienteId, MultipartFile imagen) {
        ClienteResponse response = new ClienteResponse();

        try {
            // Buscar cliente por ID
            Cliente cliente = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            // Guardar imagen en la base de datos
            Imagen imagenGuardada = new Imagen();
            imagenGuardada.setNombre(imagen.getOriginalFilename());
            imagenGuardada.setContenido(imagen.getBytes());
            Imagen savedImagen = imagenRepository.save(imagenGuardada);

            // Configurar la URL de la imagen en el cliente
            cliente.setFotoPerfil("http://localhost:9999/api/cliente/" + savedImagen.getId() + "/imagen");
            clienteRepository.save(cliente);

            response.setMessage("Imagen de perfil agregada exitosamente");
            response.setValid(true);
            response.setCliente(ClienteMapper.toDTO(cliente));
        } catch (IOException e) {
            response.setMessage("Error al procesar la imagen: " + e.getMessage());
            response.setValid(false);
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setValid(false);
        }

        return response;
    }

    public byte[] obtenerImagenPerfil(Long imagenId) {
        return imagenRepository.findById(imagenId)
                .map(Imagen::getContenido)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
    }

    public ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO) throws Exception {
        try {
            Optional<Cliente> optionalCliente = clienteRepository.findById(id);
            if (optionalCliente.isPresent()) {
                Cliente cliente = optionalCliente.get();
                cliente.setNombreCompleto(clienteDTO.getNombreCompleto());
                cliente.setTipoDocumento(clienteDTO.getTipoDocumento());
                cliente.setNumeroDocumento(clienteDTO.getNumeroDocumento());
                cliente.setNumeroTelefono(clienteDTO.getNumeroTelefono());
                cliente.setEmail(clienteDTO.getEmail());
                cliente.setNombreUsuario(clienteDTO.getNombreUsuario());
                cliente.setContrasena(clienteDTO.getContrasena());
                cliente.setPresupuesto(clienteDTO.getPresupuesto());
                cliente = clienteRepository.save(cliente);
                return ClienteMapper.toDTO(cliente);
            } else {
                throw new Exception("Cliente no encontrado");
            }
        } catch (Exception e) {
            throw new Exception("Error al actualizar cliente: " + e.getMessage(), e);
        }
    }

    public List<ClienteDTO> obtenerTodasLasEmpresas() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(ClienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO obtenerClientePorId(Long id) throws Exception {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        if (optionalCliente.isPresent()) {
            return ClienteMapper.toDTO(optionalCliente.get());
        } else {
            throw new Exception("Cliente no encontrado");
        }
    }
}
