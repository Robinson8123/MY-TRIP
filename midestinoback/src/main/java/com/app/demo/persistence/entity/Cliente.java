package com.app.demo.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Clientes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente; // ID del cliente

    private String tipoUsuario;
    private String fotoPerfil;
    private String nombreCompleto; // Nombre completo
    private String tipoDocumento; // Tipo de documento (ej. DNI, Pasaporte)
    @Column(unique = true)
    private String numeroDocumento; // Número de documento
    @Column(unique = true)
    private String numeroTelefono; // Número de teléfono
    @Column(unique = true)
    private String email; // Email
    @Column(unique = true)
    private String nombreUsuario; // Nombre de usuario
    private String contrasena; // Contraseña
    private BigDecimal presupuesto; // Presupuesto
}
