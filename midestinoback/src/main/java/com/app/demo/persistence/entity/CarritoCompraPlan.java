package com.app.demo.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CarritosCompraPlanes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoCompraPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID único del carrito

    @ManyToOne
    @JoinColumn(name = "plan_empresa_id", nullable = false)
    private PlanEmpresa planEmpresa; // El plan comprado

    @Column(nullable = false)
    private Integer cantidad; // Cantidad de planes comprados

    @Column(nullable = false)
    private BigDecimal precioTotal; // Precio total de la compra de este plan

    @Column(nullable = false)
    private boolean fueAprobado; // Si la compra fue aprobada

    @Column(nullable = false)
    private boolean eliminado; // Si el plan fue eliminado de la compra

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente; // Cliente que realiza la compra

    @ManyToOne
    @JoinColumn(name = "compra_plan_id", nullable = true)
    private CompraPlan compraPlan; // Relación con la compra general

    @PrePersist
    @PreUpdate
    public void calcularPrecioTotal() {
        if (planEmpresa != null && cantidad != null && planEmpresa.getPrecio() != null) {
            BigDecimal precio = (planEmpresa.getPrecio());
            this.precioTotal = precio.multiply(new BigDecimal(cantidad));
        } else {
            this.precioTotal = BigDecimal.ZERO; // Manejo de errores si no hay precio o cantidad
        }
    }

}
