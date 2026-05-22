package com.app.demo.domain.mapper;

import com.app.demo.domain.dto.CompraPlanDTO;
import com.app.demo.persistence.entity.CompraPlan;

public class CompraPlanMapper {

    public static CompraPlanDTO toDTO(CompraPlan compraPlan) {
        return CompraPlanDTO.builder()
                .idPlanGuardado(compraPlan.getIdPlanGuardado())
                .nombrePlan(compraPlan.getNombrePlan())
                .cliente(compraPlan.getCliente())
                .nombrePlanes(compraPlan.getNombrePlanes())
                .empresas(compraPlan.getEmpresas())
                .nombresPlanes(compraPlan.getTipoSitios())
                .tipoSitios(compraPlan.getTipoSitios())
                .direcciones(compraPlan.getDirecciones())
                .cantidadesCompradas(compraPlan.getCantidadesCompradas())
                .horarios(compraPlan.getHorarios())
                .correos(compraPlan.getCorreos())
                .ciudades(compraPlan.getCiudades())
                .telefonos(compraPlan.getTelefonos())
                .imagenes(compraPlan.getImagenes())
                .precios(compraPlan.getPrecios())
                .informacionesGenerales(compraPlan.getInformacionesGenerales())
                .estado(compraPlan.getEstado())
                .fechaCompra(compraPlan.getFechaCompra())
                .precioTotalCompra(compraPlan.getPrecioTotalCompra())
                .personasDisponibles(compraPlan.getPersonasDisponibles())
                .planesPorEmpresa(compraPlan.getPlanesPorEmpresa())
                .build();
    }

    public static CompraPlan toEntity(CompraPlanDTO compraPlanDTO) {
        return CompraPlan.builder()
                .idPlanGuardado(compraPlanDTO.getIdPlanGuardado())
                .nombrePlan(compraPlanDTO.getNombrePlan())
                .cliente(compraPlanDTO.getCliente())
                .nombrePlanes(compraPlanDTO.getNombrePlanes())
                .empresas(compraPlanDTO.getEmpresas())
                .tipoSitios(compraPlanDTO.getTipoSitios())
                .direcciones(compraPlanDTO.getDirecciones())
                .cantidadesCompradas(compraPlanDTO.getCantidadesCompradas())
                .horarios(compraPlanDTO.getHorarios())
                .correos(compraPlanDTO.getCorreos())
                .ciudades(compraPlanDTO.getCiudades())
                .telefonos(compraPlanDTO.getTelefonos())
                .imagenes(compraPlanDTO.getImagenes())
                .precios(compraPlanDTO.getPrecios())
                .informacionesGenerales(compraPlanDTO.getInformacionesGenerales())
                .personasDisponibles(compraPlanDTO.getPersonasDisponibles())
                .estado(compraPlanDTO.getEstado())
                .fechaCompra(compraPlanDTO.getFechaCompra())
                .precioTotalCompra(compraPlanDTO.getPrecioTotalCompra())
                .planesPorEmpresa(compraPlanDTO.getPlanesPorEmpresa())
                .build();
    }
}
