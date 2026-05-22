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
public class ChatRequestDTO {

    private String mensajeUsuario;
    private BigDecimal presupuestoMaximo;
    private Integer duracionDias;
    private List<String> intereses;
    private Integer numeroPersonas;
    private Long clienteId;
    private String ciudadDestino;
    private List<ChatMessageDTO> historialConversacion;
}
