package com.app.demo.domain.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.demo.domain.dto.PlanEmpresaDTO;
import com.app.demo.domain.dto.ValoracionPlanDTO;
import com.app.demo.domain.mapper.PlanEmpresaMapper;
import com.app.demo.domain.mapper.ValoracionPlanMapper;
import com.app.demo.domain.response.PlanEmpresaResponse;
import com.app.demo.domain.response.ValoracionPlanResponse;
import com.app.demo.persistence.entity.Cliente;
import com.app.demo.persistence.entity.Empresa;
import com.app.demo.persistence.entity.Imagen;
import com.app.demo.persistence.entity.PlanEmpresa;
import com.app.demo.persistence.entity.ValoracionPlan;
import com.app.demo.persistence.repository.EmpresaRepository;
import com.app.demo.persistence.repository.ImagenRepository;
import com.app.demo.persistence.repository.PlanEmpresaRepository;
import com.app.demo.persistence.repository.ValoracionPlanRepository;

import jakarta.transaction.Transactional;

@Service
public class PlanEmpresaService {

    @Autowired
    private PlanEmpresaRepository planEmpresaRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private ValoracionPlanRepository valoracionRepository;
    
    @Autowired
    private com.app.demo.persistence.repository.ClienteRepository clienteRepository;

    public PlanEmpresaResponse agregar(PlanEmpresaDTO dto, MultipartFile imagen) {
        try {

            Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                    .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
            
            dto.setDisponible(true);

            // Guardar imagen
            Imagen imagenGuardada = new Imagen();
            imagenGuardada.setNombre(imagen.getOriginalFilename());
            imagenGuardada.setContenido(imagen.getBytes());

            Imagen savedImagen = imagenRepository.save(imagenGuardada);

            // Crear el PlanEmpresa
            PlanEmpresa planEmpresa = PlanEmpresaMapper.toEntity(dto, empresa);
            planEmpresa.setImagen("http://localhost:9999/api/planes/" + savedImagen.getId() + "/imagen");
            PlanEmpresa savedPlan = planEmpresaRepository.save(planEmpresa);

            return PlanEmpresaResponse.builder()
                    .message("Plan agregado exitosamente")
                    .valid(true)
                    .planEmpresa(PlanEmpresaMapper.toDto(savedPlan))
                    .build();
        } catch (IOException e) {
            return PlanEmpresaResponse.builder()
                    .message("Error al procesar la imagen: " + e.getMessage())
                    .valid(false)
                    .build();
        } catch (Exception e) {
            return PlanEmpresaResponse.builder()
                    .message("Error: " + e.getMessage())
                    .valid(false)
                    .build();
        }
    }

    public byte[] obtenerImagen(Long id) {
        return imagenRepository.findById(id)
                .map(Imagen::getContenido)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
    }

    public PlanEmpresaResponse eliminar(Long id) {
        try {
            Optional<PlanEmpresa> optionalPlan = planEmpresaRepository.findById(id);
            if (optionalPlan.isPresent()) {
                PlanEmpresa plan = optionalPlan.get();
                plan.setDisponible(false);
                planEmpresaRepository.save(plan);
                return PlanEmpresaResponse.builder()
                        .message("Plan eliminado exitosamente")
                        .valid(true)
                        .build();
            } else {
                return PlanEmpresaResponse.builder()
                        .message("Plan no encontrado")
                        .valid(false)
                        .build();
            }
        } catch (Exception e) {
            return PlanEmpresaResponse.builder()
                    .message("Error: " + e.getMessage())
                    .valid(false)
                    .build();
        }
    }

    public PlanEmpresaResponse actualizar(PlanEmpresaDTO dto) {
        try {

            Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                    .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

            PlanEmpresa planEmpresa = PlanEmpresaMapper.toEntity(dto, empresa);
            PlanEmpresa updatedPlan = planEmpresaRepository.save(planEmpresa);

            return PlanEmpresaResponse.builder()
                    .message("Plan actualizado exitosamente")
                    .valid(true)
                    .planEmpresa(PlanEmpresaMapper.toDto(updatedPlan))
                    .build();
        } catch (Exception e) {
            return PlanEmpresaResponse.builder()
                    .message("Error: " + e.getMessage())
                    .valid(false)
                    .build();
        }
    }

    public PlanEmpresaResponse obtenerTodos() {
        try {
            List<PlanEmpresaDTO> planes = planEmpresaRepository.findAll()
                    .stream()
                    .filter(PlanEmpresa::isDisponible)
                    .map(PlanEmpresaMapper::toDto)
                    .collect(Collectors.toList());
            return PlanEmpresaResponse.builder()
                    .message("Lista de planes obtenida exitosamente")
                    .valid(true)
                    .planesList(planes)
                    .build();
        } catch (Exception e) {
            return PlanEmpresaResponse.builder()
                    .message("Error: " + e.getMessage())
                    .valid(false)
                    .build();
        }
    }

    public PlanEmpresaResponse obtenerPorEmpresa(Long empresaId) {
        try {
            Empresa empresa = empresaRepository.findById(empresaId)
                    .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
            List<PlanEmpresaDTO> planes = planEmpresaRepository.findByEmpresa(empresa)
                    .stream()
                    .filter(PlanEmpresa::isDisponible)
                    .map(PlanEmpresaMapper::toDto)
                    .collect(Collectors.toList());
            return PlanEmpresaResponse.builder()
                    .message("Lista de planes por empresa obtenida exitosamente")
                    .valid(true)
                    .planesList(planes)
                    .build();
        } catch (Exception e) {
            return PlanEmpresaResponse.builder()
                    .message("Error: " + e.getMessage())
                    .valid(false)
                    .build();
        }
    }

    @Transactional
    public void agregarValoracion(Long planEmpresaId, Long clienteId, Integer puntuacion, String comentario) {
        PlanEmpresa plan = planEmpresaRepository.findById(planEmpresaId)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));

        // Siempre crear una nueva valoración (permitir múltiples valoraciones del mismo cliente)
        ValoracionPlan valoracion = new ValoracionPlan();
        valoracion.setPlanEmpresa(plan);
        
        // Asignar la entidad Cliente
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        valoracion.setCliente(cliente);
        valoracion.setPuntuacion(puntuacion);
        valoracion.setComentario(comentario);
        valoracionRepository.save(valoracion);

        // Actualizar el promedio de valoraciones del plan
        actualizarValoracionPromedio(plan);
    }

    public ValoracionPlanResponse obtenerValoracionesPorPlan(Long planEmpresaId) {
        try {
            List<ValoracionPlan> valoraciones = valoracionRepository.findByPlanEmpresaId(planEmpresaId);
            List<ValoracionPlanDTO> dtos = valoraciones.stream()
                    .map(ValoracionPlanMapper::toDto)
                    .collect(Collectors.toList());
            return ValoracionPlanResponse.builder()
                    .message("Valoraciones obtenidas exitosamente")
                    .valid(true)
                    .valoracionesList(dtos)
                    .build();
        } catch (Exception e) {
            return ValoracionPlanResponse.builder()
                    .message("Error: " + e.getMessage())
                    .valid(false)
                    .build();
        }
    }

    private void actualizarValoracionPromedio(PlanEmpresa plan) {
        Double promedio = valoracionRepository.calcularPromedioValoracion(plan.getId());
        plan.setValoracionPromedio(promedio);
        planEmpresaRepository.save(plan);
    }

}
