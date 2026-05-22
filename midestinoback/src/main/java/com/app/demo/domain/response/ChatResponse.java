package com.app.demo.domain.response;

import java.util.List;

import com.app.demo.domain.dto.SuggestedPlanDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatResponse {

    private String message;
    private boolean valid;
    private String respuesta;
    private List<SuggestedPlanDTO> planesSugeridos;
}
