package com.app.demo.domain.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.demo.domain.dto.CompraPlanDTO;
import com.app.demo.domain.mapper.CompraPlanMapper;
import com.app.demo.domain.response.CompraPlanResponse;
import com.app.demo.persistence.entity.CarritoCompraPlan;
import com.app.demo.persistence.entity.Cliente;
import com.app.demo.persistence.entity.CompraPlan;
import com.app.demo.persistence.entity.Empresa;
import com.app.demo.persistence.entity.PlanEmpresa;
import com.app.demo.persistence.repository.CarritoCompraPlanRepository;
import com.app.demo.persistence.repository.ClienteRepository;
import com.app.demo.persistence.repository.CompraPlanRepository;
import com.app.demo.persistence.repository.EmpresaRepository;
import com.app.demo.persistence.repository.PlanEmpresaRepository;

import jakarta.transaction.Transactional;

@Service
public class CompraPlanService {

    @Autowired private CompraPlanRepository compraPlanRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private PlanEmpresaRepository planEmpresaRepository;
    @Autowired private CarritoCompraPlanRepository carritoRepository;

    @Transactional
    public CompraPlanResponse agregarCompraPlan(CompraPlanDTO dto) {
        try {
            if (dto == null || dto.getCliente() == null || dto.getPlanesPorEmpresa() == null
                    || dto.getCantidadesCompradas() == null) {
                return new CompraPlanResponse("Datos inválidos en la solicitud", false, null, null);
            }

            if (dto.getPlanesPorEmpresa().size() != dto.getCantidadesCompradas().size()) {
                return new CompraPlanResponse("Las listas de planes y cantidades no coinciden en tamaño", false, null, null);
            }

            if (dto.getPlanesPorEmpresa().isEmpty()) {
                return new CompraPlanResponse("La compra debe contener al menos un plan", false, null, null);
            }

            Cliente cliente = dto.getCliente();
            if (cliente == null || cliente.getIdCliente() == null) {
                return new CompraPlanResponse("Cliente no válido en la solicitud", false, null, null);
            }

            // FIX línea 61: extraer el id validado antes de pasarlo a findById (@NonNull)
            Long clienteId = Objects.requireNonNull(cliente.getIdCliente());
            cliente = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado en base de datos"));

            BigDecimal total = dto.getPrecioTotalCompra();
            if (total == null || total.compareTo(BigDecimal.ZERO) <= 0) {
                return new CompraPlanResponse("El precio total debe ser mayor a 0", false, null, null);
            }

            if (cliente.getPresupuesto().compareTo(total) < 0) {
                return new CompraPlanResponse("Presupuesto insuficiente", false, null, null);
            }

            CompraPlan entity = new CompraPlan();
            entity.setCliente(cliente);
            entity.setFechaCompra(LocalDate.now().toString());
            entity.setPrecioTotalCompra(total);
            CompraPlan guardado = compraPlanRepository.save(entity);

            cliente.setPresupuesto(cliente.getPresupuesto().subtract(total));
            clienteRepository.save(cliente);

            Cliente admin = clienteRepository.findByEmail("admin@midestino.com")
                    .orElseThrow(() -> new RuntimeException("Cuenta de administrador no encontrada"));

            BigDecimal totalComisionAdmin = BigDecimal.ZERO;

            for (int i = 0; i < dto.getPlanesPorEmpresa().size(); i++) {
                Long idPlan = dto.getPlanesPorEmpresa().get(i);
                if (idPlan == null) {
                    return new CompraPlanResponse("ID de plan inválido en posición " + i, false, null, null);
                }

                String cantStr = dto.getCantidadesCompradas().get(i);
                Integer cant = convertirAInteger(cantStr);
                if (cant == null || cant <= 0) {
                    return new CompraPlanResponse("Cantidad en posición " + i + " debe ser mayor a 0", false, null, null);
                }

                PlanEmpresa plan = planEmpresaRepository.findById(idPlan)
                        .orElseThrow(() -> new RuntimeException("Plan no encontrado con ID: " + idPlan));

                plan.setCantidadDisponible(plan.getCantidadDisponible() - cant);
                planEmpresaRepository.save(plan);

                BigDecimal gananciaPlan = plan.getPrecio().multiply(BigDecimal.valueOf(cant));
                BigDecimal comision = gananciaPlan.multiply(new BigDecimal("0.10"));
                BigDecimal gananciaNetaEmpresa = gananciaPlan.multiply(new BigDecimal("0.90"));

                Empresa emp = plan.getEmpresa();
                if (emp == null) {
                    throw new RuntimeException("Empresa no asociada al plan: " + idPlan);
                }
                BigDecimal gananciaActual = emp.getGanancias() != null ? emp.getGanancias() : BigDecimal.ZERO;
                emp.setGanancias(gananciaActual.add(gananciaNetaEmpresa));
                empresaRepository.save(emp);

                totalComisionAdmin = totalComisionAdmin.add(comision);
            }

            BigDecimal presupuestoAdmin = admin.getPresupuesto() != null ? admin.getPresupuesto() : BigDecimal.ZERO;
            admin.setPresupuesto(presupuestoAdmin.add(totalComisionAdmin));
            clienteRepository.save(admin);

            eliminarDelCarritoCompras(cliente.getIdCliente());

            CompraPlanDTO respuestaDTO = CompraPlanMapper.toDTO(guardado);
            return new CompraPlanResponse("Compra procesada con éxito", true, respuestaDTO, null);

        } catch (RuntimeException e) {
            return new CompraPlanResponse("Error en proceso de compra: " + e.getMessage(), false, null, null);
        } catch (Exception e) {
            return new CompraPlanResponse("Error inesperado: " + e.getMessage(), false, null, null);
        }
    }

    public CompraPlanResponse obtenerComprasPorCliente(Long clienteId) {
        try {
            if (clienteId == null || clienteId <= 0) {
                return new CompraPlanResponse("ID de cliente inválido", false, null, null);
            }

            List<CompraPlan> compras = compraPlanRepository.findByClienteIdCliente(clienteId);

            if (compras == null || compras.isEmpty()) {
                return new CompraPlanResponse("No hay compras para este cliente", false, null, null);
            }

            List<CompraPlanDTO> dtos = compras.stream()
                    .map(CompraPlanMapper::toDTO)
                    .collect(Collectors.toList());

            return new CompraPlanResponse("Compras obtenidas", true, null, dtos);
        } catch (Exception e) {
            return new CompraPlanResponse("Error al obtener compras: " + e.getMessage(), false, null, null);
        }
    }

    public CompraPlanResponse obtenerComprasPorEmpresa(Long empresaId) {
        try {
            if (empresaId == null || empresaId <= 0) {
                return new CompraPlanResponse("ID de empresa inválido", false, null, null);
            }

            List<CompraPlan> compras = compraPlanRepository.findByEmpresaIdEmpresa(empresaId);

            if (compras == null || compras.isEmpty()) {
                return new CompraPlanResponse("No hay ventas para esta empresa", false, null, null);
            }

            List<CompraPlanDTO> dtos = compras.stream()
                    .map(CompraPlanMapper::toDTO)
                    .collect(Collectors.toList());

            return new CompraPlanResponse("Ventas de la empresa obtenidas", true, null, dtos);
        } catch (Exception e) {
            return new CompraPlanResponse("Error al obtener ventas: " + e.getMessage(), false, null, null);
        }
    }

    private void eliminarDelCarritoCompras(Long idCliente) {
        try {
            if (idCliente == null || idCliente <= 0) {
                return;
            }

            List<CarritoCompraPlan> items = carritoRepository.findByClienteIdCliente(idCliente);
            if (items != null && !items.isEmpty()) {
                items.forEach(item -> {
                    item.setEliminado(true);
                    item.setFueAprobado(true);
                });
                carritoRepository.saveAll(items);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al limpiar carrito: " + e.getMessage(), e);
        }
    }

    /**
     * Convierte un objeto a Integer de forma segura usando instanceof pattern matching.
     */
    private Integer convertirAInteger(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            // FIX: instanceof pattern matching (Java 16+), elimina casts redundantes
            if (obj instanceof Integer i) {
                return i;
            } else if (obj instanceof String s) {
                return Integer.parseInt(s);   // FIX: sin cast innecesario
            } else if (obj instanceof Long l) {
                return l.intValue();
            } else if (obj instanceof Double d) {
                return d.intValue();
            } else if (obj instanceof Number n) {
                return n.intValue();
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return null;
    }

    // convertirALong y convertirABigDecimal ELIMINADOS: nunca se usaban en este servicio.
}