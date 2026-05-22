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
public class EmpresaDTO {

    private Long idEmpresa;
    private String nombre;
    private String fotoPerfil;
    private String razonSocial;
    private String nit;
    private String sector;
    private String fechaFundacion;
    private String direccion;
    private String ciudad;
    private String telefono;
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
    private boolean confirmacion;
    private BigDecimal ganancias;
    private String firmaRepresentanteLegal;
    private String fechaFirma;
    private String tipoUsuario;
    private boolean empresaFueAceptada;
    private boolean empresaFueAceptadaPorAdmin;
    private boolean empresaTuvoRespuesta;
    private String contrasena;
}
