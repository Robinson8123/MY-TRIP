package com.app.demo.persistence.repository;

import com.app.demo.persistence.entity.CarritoCompraPlan;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoCompraPlanRepository extends JpaRepository<CarritoCompraPlan, Long> {

    List<CarritoCompraPlan> findByClienteIdCliente(Long clienteId);

    Optional<CarritoCompraPlan> findByClienteIdClienteAndPlanEmpresaId(Long clienteId, Long planEmpresaId);

    boolean existsByClienteIdClienteAndPlanEmpresaIdAndEliminadoFalse(Long clienteId, Long planEmpresaId);
}
