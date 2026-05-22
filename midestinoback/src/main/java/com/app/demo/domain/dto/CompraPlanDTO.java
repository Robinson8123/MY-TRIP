package com.app.demo.domain.dto;

import com.app.demo.persistence.entity.Cliente;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompraPlanDTO {

    private Long idPlanGuardado;
    private String nombrePlan;
    private Cliente cliente;
    private List<String> nombrePlanes;
    private List<Long> empresas;
    private List<String> nombresPlanes;
    private List<String> tipoSitios;
    private List<String> direcciones;
    private List<String> cantidadesCompradas;
    private List<String> horarios;
    private List<String> correos;
    private List<String> ciudades;
    private List<Long> telefonos;
    private List<String> imagenes;
    private List<BigDecimal> precios;
    private List<String> informacionesGenerales;
    private List<Long> planesPorEmpresa;
    private String estado;
    private String fechaCompra;
    private Integer personasDisponibles;
    private BigDecimal precioTotalCompra;
}
