package com.app.demo.domain.mapper;

import com.app.demo.domain.dto.CarritoCompraPlanDTO;
import com.app.demo.domain.dto.ClienteDTO;
import com.app.demo.domain.dto.PlanEmpresaDTO;
import com.app.demo.persistence.entity.CarritoCompraPlan;

public class CarritoCompraPlanMapper {

    public static CarritoCompraPlanDTO toDTO(CarritoCompraPlan carrito) {
        CarritoCompraPlanDTO dto = new CarritoCompraPlanDTO();
        dto.setId(carrito.getId());

        PlanEmpresaDTO planDTO = PlanEmpresaMapper.toDto(carrito.getPlanEmpresa());
        dto.setPlanEmpresa(planDTO);

        ClienteDTO clienteDTO = ClienteMapper.toDTO(carrito.getCliente());
        dto.setCliente(clienteDTO);

        dto.setCantidad(carrito.getCantidad());
        dto.setPrecioTotal(carrito.getPrecioTotal());
        dto.setFueAprobado(carrito.isFueAprobado());
        dto.setEliminado(carrito.isEliminado());
        return dto;
    }

    public static CarritoCompraPlan toEntity(CarritoCompraPlanDTO dto) {
        CarritoCompraPlan carrito = new CarritoCompraPlan();
        carrito.setId(dto.getId());
        carrito.setCantidad(dto.getCantidad());
        carrito.setFueAprobado(dto.isFueAprobado());
        carrito.setPrecioTotal(dto.getPrecioTotal());
        carrito.setEliminado(dto.isEliminado());
        return carrito;
    }
}
