package com.app.demo.domain.response;

import com.app.demo.domain.dto.EmpresaDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaResponse {

    private String message;
    private boolean valid;
    private EmpresaDTO empresa;  // Single object response
    private List<EmpresaDTO> empresasList; // List of objects response
}
