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
public class RecomendacionDTO {

    private Long planId;
    private String nombre;
    private String tipoSitio;
    private String ciudad;
    private BigDecimal precio;
    private Double valoracionPromedio;
    private String imagenUrl;
    private String metodoRecomendacion; // "colaborativo", "contenido", "popular"
    private Double scoreRelevancia;
}
