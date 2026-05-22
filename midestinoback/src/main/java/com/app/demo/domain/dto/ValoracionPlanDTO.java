package com.app.demo.domain.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValoracionPlanDTO {
    private Long id;
    private Long planEmpresaId;
    private Long clienteId;
    private Integer puntuacion;
    private String comentario;
    private LocalDateTime fecha;
}
