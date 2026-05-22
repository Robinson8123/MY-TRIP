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
public class PlanEmpresaDTO {

    private Long id;
    private String nombre;
    private String tipoSitio;
    private String direccion;
    private String horario;
    private String email;
    private String ciudad;
    private String telefono;
    private BigDecimal precio;
    private Integer cantidadDisponible;
    private Integer personasDisponibles;
    private String informacionGeneral;
    private String imagen;
    private Double valoracionPromedio;
    private String metodosPagoAceptados;
    private String fechaRegistro;
    private boolean disponible;
    private Long empresaId;
}
