package com.app.demo.domain.service;

import com.app.demo.domain.dto.EmpresaDTO;
import com.app.demo.domain.mapper.EmpresaMapper;
import com.app.demo.domain.response.EmpresaResponse;
import com.app.demo.persistence.entity.Empresa;
import com.app.demo.persistence.entity.Imagen;
import com.app.demo.persistence.repository.EmpresaRepository;
import com.app.demo.persistence.repository.ImagenRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ImagenRepository imagenRepository;

    public EmpresaResponse iniciarSesion(EmpresaDTO empresaDTO) {
        EmpresaResponse response = new EmpresaResponse();

        try {
            Optional<Empresa> optionalEmpresa = empresaRepository.findByCorreoAndContrasenaAndEmpresaFueAceptada(
                    empresaDTO.getCorreo(),
                    empresaDTO.getContrasena(),
                    true // Cambia esto si quieres validar otro campo
            );

            if (optionalEmpresa.isPresent()) {
                response.setMessage("Inicio de sesión exitoso");
                response.setValid(true);
                response.setEmpresa(EmpresaMapper.toDTO(optionalEmpresa.get()));
            } else {
                response.setMessage("Credenciales incorrectas o la empresa aún no ha sido aceptada por un administrador.");
                response.setValid(false);
            }
        } catch (Exception e) {
            response.setMessage("Error al iniciar sesión: " + e.getMessage());
            response.setValid(false);
        }

        return response;
    }

    public EmpresaDTO agregarEmpresa(EmpresaDTO empresaDTO) throws Exception {
        try {

            if (empresaRepository.existsByNombre(empresaDTO.getNombre())) {
                throw new Exception("El nombre de la empresa ya está registrado.");
            }
            if (empresaRepository.existsByTelefono(empresaDTO.getTelefono())) {
                throw new Exception("El teléfono de la empresa ya está registrado.");
            }
            if (empresaRepository.existsByCorreo(empresaDTO.getCorreo())) {
                throw new Exception("El correo de la empresa ya está registrado.");
            }

            Empresa empresa = EmpresaMapper.toEntity(empresaDTO);
            empresa.setGanancias(BigDecimal.valueOf(0));
            empresa = empresaRepository.save(empresa);
            return EmpresaMapper.toDTO(empresa);
        } catch (Exception e) {
            throw new Exception("Error al agregar empresa: " + e.getMessage(), e);
        }
    }

    public EmpresaResponse agregarImagenPerfil(Long empresaId, MultipartFile imagen) {
        EmpresaResponse response = new EmpresaResponse();

        try {
            // Buscar cliente por ID
            Empresa empresa = empresaRepository.findById(empresaId)
                    .orElseThrow(() -> new RuntimeException("Empresa no encontrado"));

            // Guardar imagen en la base de datos
            Imagen imagenGuardada = new Imagen();
            imagenGuardada.setNombre(imagen.getOriginalFilename());
            imagenGuardada.setContenido(imagen.getBytes());
            Imagen savedImagen = imagenRepository.save(imagenGuardada);

            // Configurar la URL de la imagen en el cliente
            empresa.setFotoPerfil("http://localhost:9999/api/empresa/" + savedImagen.getId() + "/imagen");
            empresaRepository.save(empresa);

            response.setMessage("Imagen de perfil agregada exitosamente");
            response.setValid(true);
            response.setEmpresa(EmpresaMapper.toDTO(empresa));
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

    public EmpresaDTO actualizarEmpresa(Long id, EmpresaDTO empresaDTO) throws Exception {
        try {
            Optional<Empresa> optionalEmpresa = empresaRepository.findById(id);
            if (optionalEmpresa.isPresent()) {
                Empresa empresa = optionalEmpresa.get();
                empresa = EmpresaMapper.toEntity(empresaDTO); // Map new values to existing entity
                empresa.setIdEmpresa(id); // Ensure the ID is preserved

                if (empresa.isEmpresaFueAceptada()) {
                    empresa.setEmpresaFueAceptadaPorAdmin(true);
                }

                empresa = empresaRepository.save(empresa);
                return EmpresaMapper.toDTO(empresa);
            } else {
                throw new Exception("Empresa no encontrada");
            }
        } catch (Exception e) {
            throw new Exception("Error al actualizar empresa: " + e.getMessage(), e);
        }
    }

    public List<EmpresaDTO> obtenerTodasLasEmpresas() {
        List<Empresa> empresas = empresaRepository.findAll();
        return empresas.stream()
                .map(EmpresaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public EmpresaDTO obtenerEmpresaPorId(Long id) throws Exception {
        Optional<Empresa> optionalEmpresa = empresaRepository.findById(id);
        if (optionalEmpresa.isPresent()) {
            return EmpresaMapper.toDTO(optionalEmpresa.get());
        } else {
            throw new Exception("Empresa no encontrada");
        }
    }
}
