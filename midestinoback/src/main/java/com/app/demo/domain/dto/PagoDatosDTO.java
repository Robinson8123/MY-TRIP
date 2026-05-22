package com.app.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoDatosDTO {
    // Identificadores de la cuenta PayU
    private String merchantId;
    private String accountId;
    
    // Datos de la transacción
    private String referencia;
    private String monto; // Cambiado de Long (centavos) a String (decimales)
    private String moneda;
    private String descripcion;
    
    // Seguridad y Configuración
    private String firma;
    private String urlPrueba; // Opcional: para manejar la URL de Sandbox desde el backend
    
    // Datos del comprador (opcionales pero recomendados para PayU)
    private String emailComprador;
    private String nombreComprador;
}