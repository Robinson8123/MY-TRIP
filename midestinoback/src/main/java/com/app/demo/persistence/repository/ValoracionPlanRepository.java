package com.app.demo.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.demo.persistence.entity.ValoracionPlan;

public interface ValoracionPlanRepository extends JpaRepository<ValoracionPlan, Long> {

    @Query("SELECT AVG(v.puntuacion) FROM ValoracionPlan v WHERE v.planEmpresa.id = :planEmpresaId")
    Double calcularPromedioValoracion(@Param("planEmpresaId") Long planEmpresaId);

    ValoracionPlan findByPlanEmpresaIdAndCliente_IdCliente(Long planEmpresaId, Long clienteId);

    // Obtener todas las reseñas/valoraciones de un plan
    List<ValoracionPlan> findByPlanEmpresaId(Long planEmpresaId);
}
