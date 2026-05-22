package com.app.demo.persistence.repository;

import com.app.demo.persistence.entity.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByEmailAndContrasena(String email, String contrasena);

    boolean existsByNumeroDocumento(String numeroDocumento);

    boolean existsByNumeroTelefono(String numeroTelefono);

    boolean existsByEmail(String email);

    boolean existsByNombreUsuario(String nombreUsuario);

    Optional<Cliente> findByEmail(String email);
}
