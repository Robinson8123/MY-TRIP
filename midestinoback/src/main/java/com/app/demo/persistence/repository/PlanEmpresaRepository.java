package com.app.demo.persistence.repository;

import com.app.demo.persistence.entity.Empresa;
import com.app.demo.persistence.entity.PlanEmpresa;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanEmpresaRepository extends JpaRepository<PlanEmpresa, Long> {

    List<PlanEmpresa> findByEmpresa(Empresa empresa);

    List<PlanEmpresa> findByDisponibleTrueAndDireccionContainingIgnoreCase(String ciudad);

    List<PlanEmpresa> findByDisponibleTrueAndCiudadIgnoreCase(String ciudad);

    List<PlanEmpresa> findByDisponibleTrueAndEmpresa_CiudadIgnoreCase(String ciudad);

    List<PlanEmpresa> findByDisponibleTrueAndEmpresa_CiudadContainingIgnoreCase(String ciudad);

    Optional<PlanEmpresa> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
