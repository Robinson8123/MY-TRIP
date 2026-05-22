package com.app.demo.domain.response;

import com.app.demo.domain.dto.PlanEmpresaDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanEmpresaResponse {

    private String message;
    private boolean valid;
    private PlanEmpresaDTO planEmpresa;
    private List<PlanEmpresaDTO> planesList;
}
