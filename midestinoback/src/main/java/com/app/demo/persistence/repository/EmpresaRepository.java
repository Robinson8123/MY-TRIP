package com.app.demo.persistence.repository;

import com.app.demo.persistence.entity.Empresa;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Optional<Empresa> findByCorreoAndContrasena(String email, String contrasena);

    Optional<Empresa> findByCorreoAndContrasenaAndEmpresaFueAceptada(String email, String contrasena, boolean empresaFueAceptada);

    Optional<Empresa> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    boolean existsByTelefono(String telefono);

    boolean existsByCorreo(String correo);
}
