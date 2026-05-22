package com.app.demo.domain.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    private Long idCliente; // ID del cliente
    private String tipoUsuario;
    private String fotoPerfil;
    private String nombreCompleto; // Nombre completo
    private String tipoDocumento; // Tipo de documento
    private String numeroDocumento; // Número de documento
    private String numeroTelefono; // Número de teléfono
    private String email; // Email
    private String nombreUsuario; // Nombre de usuario
    private String contrasena; // Contraseña
    private BigDecimal presupuesto; // Presupuesto
}
