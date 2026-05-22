package com.app.demo.domain.response;

import java.util.List;

import com.app.demo.domain.dto.ValoracionPlanDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValoracionPlanResponse {

    private String message;
    private boolean valid;
    private ValoracionPlanDTO valoracion;
    private List<ValoracionPlanDTO> valoracionesList;
}
