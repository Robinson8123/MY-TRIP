package com.app.demo.domain.mapper;

import com.app.demo.domain.dto.PlanEmpresaDTO;
import com.app.demo.persistence.entity.PlanEmpresa;
import com.app.demo.persistence.entity.Empresa;
import org.springframework.stereotype.Component;

@Component
public class PlanEmpresaMapper {

    public static PlanEmpresa toEntity(PlanEmpresaDTO dto, Empresa empresa) {
        return PlanEmpresa.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .tipoSitio(dto.getTipoSitio())
                .direccion(dto.getDireccion())
                .horario(dto.getHorario())
                .email(dto.getEmail())
                .ciudad(dto.getCiudad())
                .telefono(dto.getTelefono())
                .precio(dto.getPrecio())
                .cantidadDisponible(dto.getCantidadDisponible())
                .personasDisponibles(dto.getPersonasDisponibles())
                .informacionGeneral(dto.getInformacionGeneral())
                .imagen(dto.getImagen())
                .valoracionPromedio(dto.getValoracionPromedio())
                .metodosPagoAceptados(dto.getMetodosPagoAceptados())
                .disponible(dto.isDisponible())
                .fechaRegistro(dto.getFechaRegistro())
                .empresa(empresa)
                .build();
    }

    public static PlanEmpresaDTO toDto(PlanEmpresa entity) {
        return PlanEmpresaDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .tipoSitio(entity.getTipoSitio())
                .direccion(entity.getDireccion())
                .horario(entity.getHorario())
                .email(entity.getEmail())
                .ciudad(entity.getCiudad())
                .telefono(entity.getTelefono())
                .precio(entity.getPrecio())
                .cantidadDisponible(entity.getCantidadDisponible())
                .personasDisponibles(entity.getPersonasDisponibles())
                .informacionGeneral(entity.getInformacionGeneral())
                .imagen(entity.getImagen())
                .valoracionPromedio(entity.getValoracionPromedio())
                .metodosPagoAceptados(entity.getMetodosPagoAceptados())
                .fechaRegistro(entity.getFechaRegistro())
                .disponible(entity.isDisponible())
                .empresaId(entity.getEmpresa().getIdEmpresa())
                .build();
    }
}
