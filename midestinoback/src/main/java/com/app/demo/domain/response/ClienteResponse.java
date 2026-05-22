package com.app.demo.domain.response;

import com.app.demo.domain.dto.ClienteDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {

    private String message; // Mensaje de respuesta
    private boolean valid; // Estado de la respuesta
    private ClienteDTO cliente; // Respuesta de un cliente
    private List<ClienteDTO> listaClientes; // Respuesta de una lista de clientes
}
