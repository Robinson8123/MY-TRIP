package com.app.demo.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PlanesEmpresas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String tipoSitio;

    @Column(nullable = false, length = 255)
    private String direccion;

    @Column(nullable = false, length = 50)
    private String horario;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(name = "ciudad", nullable = false, length = 50)
    private String ciudad;

    @Column(nullable = false, length = 15)
    private String telefono;

    @Column(nullable = false)
    private BigDecimal precio; // Eliminados precision y scale

    @Column(nullable = false)
    private Integer cantidadDisponible;

    @Column(nullable = false)
    private Integer personasDisponibles;

    @Column(length = 1000)
    private String informacionGeneral;

    @Column(name = "imagen_url")
    private String imagen;

    @Column
    private String fechaRegistro;

    @Column
    private Double valoracionPromedio;

    @Column(name = "metodo_pago")
    private String metodosPagoAceptados;

    @Column(nullable = false)
    private boolean disponible;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

}
