package com.app.demo.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CompraPlanes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompraPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlanGuardado;

    private String nombrePlan;

    @ElementCollection
    @CollectionTable(name = "compra_plan_nombre_planes", joinColumns = @JoinColumn(name = "compra_plan_id"))
    @Column(name = "nombre_plan")
    private List<String> nombrePlanes;

    @ElementCollection
    @CollectionTable(name = "compra_plan_tipo_sitios", joinColumns = @JoinColumn(name = "compra_plan_id"))
    @Column(name = "tipo_sitio")
    private List<String> tipoSitios;

    @ElementCollection
    @CollectionTable(name = "compra_plan_direcciones", joinColumns = @JoinColumn(name = "compra_plan_id"))
    @Column(name = "direccion")
    private List<String> direcciones;

    @ElementCollection
    @CollectionTable(name = "compra_plan_cantidades_compradas", joinColumns = @JoinColumn(name = "compra_plan_id"))
    @Column(name = "cantidad_comprad")
    private List<String> cantidadesCompradas;

    @ElementCollection
    @CollectionTable(name = "compra_plan_horarios", joinColumns = @JoinColumn(name = "compra_plan_id"))
    @Column(name = "horario")
    private List<String> horarios;

    @ElementCollection
    @CollectionTable(name = "compra_plan_correos", joinColumns = @JoinColumn(name = "compra_plan_id"))
    @Column(name = "correo")
    private List<String> correos;

    @ElementCollection
    @CollectionTable(name = "compra_plan_ciudades", joinColumns = @JoinColumn(name = "compra_plan_id"))
    @Column(name = "ciudad")
    private List<String> ciudades;

    @ElementCollection
    @CollectionTable(name = "compra_plan_telefonos", joinColumns = @JoinColumn(name = "compra_plan_id"))
    @Column(name = "telefono")
    private List<Long> telefonos;

    @ElementCollection
    @CollectionTable(name = "compra_plan_imagenes", joinColumns = @JoinColumn(name = "compra_plan_id"))
    @Column(name = "imagen_url", length = 500)
    private List<String> imagenes;

    @ElementCollection
    @CollectionTable(name = "compra_plan_precios", joinColumns = @JoinColumn(name = "compra_plan_id"))
    @Column(name = "precio", precision = 10, scale = 2)
    private List<BigDecimal> precios;

    @ElementCollection
    @CollectionTable(name = "compra_plan_informaciones_generales", joinColumns = @JoinColumn(name = "compra_plan_id"))
    @Column(name = "informacion_general")
    private List<String> informacionesGenerales;

    @ElementCollection
    @CollectionTable(name = "compra_plan_empresas", joinColumns = @JoinColumn(name = "compra_plan_id"))
    @Column(name = "empresa_id")
    private List<Long> empresas;

    @ElementCollection
    @CollectionTable(name = "compra_plan_por_empresas", joinColumns = @JoinColumn(name = "compra_plan_id"))
    @Column(name = "plan_empresa_id")
    private List<Long> planesPorEmpresa;

    private String estado;

    private String fechaCompra;

    private Integer personasDisponibles;

    private BigDecimal precioTotalCompra;
// Agrega estos campos dentro de la clase CompraPlan
private String referenciaPago;
private String idTransaccionPago;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
}
