package com.app.demo.domain.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuggestedPlanDTO {

    private Long id;
    private String nombrePlan;
    private String tipoSitio;
    private String direccion;
    private String horario;
    private BigDecimal precio;
    private Double valoracionPromedio;
    private String empresaNombre;
    private String telefonoContacto;
    private String emailContacto;
    private String metodosPagoAceptados;
    private String informacionGeneral;
    private List<String> comentariosDestacados;
    private String ciudad;
    private String ciudadEmpresa;
}
