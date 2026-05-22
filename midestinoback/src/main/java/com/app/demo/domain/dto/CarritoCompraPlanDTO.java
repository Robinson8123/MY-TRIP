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
public class CarritoCompraPlanDTO {

    private Long id;
    private PlanEmpresaDTO planEmpresa;
    private Integer cantidad;
    private BigDecimal precioTotal;
    private boolean fueAprobado;
    private boolean eliminado;
    private ClienteDTO cliente;
}
