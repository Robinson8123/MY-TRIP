package com.app.demo.domain.response;

import com.app.demo.domain.dto.PagoDatosDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagoDatosResponse {
    private String message;
    private boolean valid;
    private PagoDatosDTO pagoDatos;
}
