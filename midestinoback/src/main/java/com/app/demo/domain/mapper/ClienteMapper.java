package com.app.demo.domain.mapper;

import com.app.demo.domain.dto.ClienteDTO;
import com.app.demo.persistence.entity.Cliente;

public class ClienteMapper {

    public static ClienteDTO toDTO(Cliente cliente) {
        return ClienteDTO.builder()
                .idCliente(cliente.getIdCliente())
                .fotoPerfil(cliente.getFotoPerfil())
                .tipoUsuario(cliente.getTipoUsuario())
                .nombreCompleto(cliente.getNombreCompleto())
                .tipoDocumento(cliente.getTipoDocumento())
                .numeroDocumento(cliente.getNumeroDocumento())
                .numeroTelefono(cliente.getNumeroTelefono())
                .email(cliente.getEmail())
                .nombreUsuario(cliente.getNombreUsuario())
                .contrasena(cliente.getContrasena())
                .presupuesto(cliente.getPresupuesto())
                .build();
    }

    public static Cliente toEntity(ClienteDTO clienteDTO) {
        return Cliente.builder()
                .idCliente(clienteDTO.getIdCliente())
                .fotoPerfil(clienteDTO.getFotoPerfil())
                .nombreCompleto(clienteDTO.getNombreCompleto())
                .tipoUsuario(clienteDTO.getTipoUsuario())
                .tipoDocumento(clienteDTO.getTipoDocumento())
                .numeroDocumento(clienteDTO.getNumeroDocumento())
                .numeroTelefono(clienteDTO.getNumeroTelefono())
                .email(clienteDTO.getEmail())
                .nombreUsuario(clienteDTO.getNombreUsuario())
                .contrasena(clienteDTO.getContrasena())
                .presupuesto(clienteDTO.getPresupuesto())
                .build();
    }
}
