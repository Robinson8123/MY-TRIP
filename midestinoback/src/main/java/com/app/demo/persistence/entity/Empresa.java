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
@Table(name = "empresas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmpresa;

    @Column(unique = true)
    private String nombre;
    private String fotoPerfil;
    private String razonSocial;
    private String nit;
    private String sector;
    private String fechaFundacion;

    private String direccion;
    private String ciudad;

    @Column(unique = true)
    private String telefono;

    @Column(unique = true)
    private String correo;
    private String web;

    private String numeroRegistroMercantil;
    private String fechaRegistro;
    private String entidadRegistro;
    private String tipoSociedad;

    private String nombreRepresentanteLegal;
    private String numeroDocumentoRepresentanteLegal;
    private String cargoPropietario;
    private String nombrePropietarioPrincipal;

    private String certificadoExistencia;
    private String RUT;
    private String estadosFinancieros;
    private String notariaRegistro;
    private String otrosDocumentosLegales;

    private boolean confirmación;
    private String firmaRepresentanteLegal;
    private String fechaFirma;

    @Column(name = "ganancias", nullable = false, precision = 38, scale = 2)
    @Builder.Default
    private BigDecimal ganancias = BigDecimal.ZERO;

    private String tipoUsuario;
    private String contrasena;
    private boolean empresaFueAceptada;
    private boolean empresaFueAceptadaPorAdmin;
    private boolean empresaTuvoRespuesta;
}