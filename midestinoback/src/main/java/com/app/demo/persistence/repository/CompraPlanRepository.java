package com.app.demo.persistence.repository;

import com.app.demo.persistence.entity.CompraPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompraPlanRepository extends JpaRepository<CompraPlan, Long> {

    // Método existente para buscar por cliente
    List<CompraPlan> findByClienteIdCliente(Long id);

    // Método para buscar por empresa
    @Query("SELECT c FROM CompraPlan c WHERE :empresaId MEMBER OF c.empresas")
    List<CompraPlan> findByEmpresaIdEmpresa(@Param("empresaId") Long empresaId);

    /**
     * SOLUCIÓN AL ERROR: findByReferenciaPago
     * Este método es necesario para que el Webhook de PayU pueda encontrar
     * la compra y actualizar su estado a "PAGADO".
     */
    Optional<CompraPlan> findByReferenciaPago(String referenciaPago);
}