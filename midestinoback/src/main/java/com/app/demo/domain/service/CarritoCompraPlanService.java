package com.app.demo.domain.service;

import com.app.demo.domain.dto.CarritoCompraPlanDTO;
import com.app.demo.domain.exception.PlanAlreadyInCarritoException;
import com.app.demo.domain.mapper.CarritoCompraPlanMapper;
import com.app.demo.domain.response.CarritoCompraPlanReponse;
import com.app.demo.persistence.entity.CarritoCompraPlan;
import com.app.demo.persistence.entity.Cliente;
import com.app.demo.persistence.entity.PlanEmpresa;
import com.app.demo.persistence.repository.CarritoCompraPlanRepository;
import com.app.demo.persistence.repository.ClienteRepository;
import com.app.demo.persistence.repository.PlanEmpresaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarritoCompraPlanService {

    @Autowired
    private CarritoCompraPlanRepository carritoRepository;

    @Autowired
    private PlanEmpresaRepository planRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public CarritoCompraPlanReponse guardarCarrito(CarritoCompraPlanDTO carritoDTO) {
        CarritoCompraPlanReponse response = new CarritoCompraPlanReponse();
        try {
            if (carritoDTO == null || carritoDTO.getPlanEmpresa() == null
                    || carritoDTO.getPlanEmpresa().getId() == null) {
                response.setMessage("Plan requerido.");
                response.setValid(false);
                return response;
            }

            if (carritoDTO.getCliente() == null || carritoDTO.getCliente().getIdCliente() == null) {
                response.setMessage("Cliente requerido.");
                response.setValid(false);
                return response;
            }

            Long planId = carritoDTO.getPlanEmpresa().getId();
            Long clienteId = carritoDTO.getCliente().getIdCliente();

            PlanEmpresa plan = planRepository.findById(planId)
                    .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado."));
            Cliente cliente = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado."));

            boolean existeActivo = carritoRepository
                    .existsByClienteIdClienteAndPlanEmpresaIdAndEliminadoFalse(clienteId, planId);

            if (existeActivo) {
                throw new PlanAlreadyInCarritoException("El plan ya está en el carrito");
            }

            int cantidad = (carritoDTO.getCantidad() != null && carritoDTO.getCantidad() > 0)
                    ? carritoDTO.getCantidad()
                    : 1;

            Optional<CarritoCompraPlan> carritoExistenteOpt = carritoRepository
                    .findByClienteIdClienteAndPlanEmpresaId(clienteId, planId);

            CarritoCompraPlan carrito;
            if (carritoExistenteOpt.isPresent()) {
                carrito = carritoExistenteOpt.get();
                carrito.setCantidad(cantidad);
                carrito.setPlanEmpresa(plan);
                carrito.setCliente(cliente);
                carrito.setEliminado(false);
                carrito.setFueAprobado(false);
                response.setMessage("Se reactivó el plan en el carrito");
            } else {
                carrito = CarritoCompraPlanMapper.toEntity(carritoDTO);
                carrito.setPlanEmpresa(plan);
                carrito.setCliente(cliente);
                carrito.setCantidad(cantidad);
                carrito.setEliminado(false);
                carrito.setFueAprobado(false);
                response.setMessage("Se agregó el producto al carrito de compras");
            }

            carritoRepository.save(carrito);

            response.setCarrito(CarritoCompraPlanMapper.toDTO(carrito));
            response.setValid(true);
        } catch (PlanAlreadyInCarritoException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            response.setMessage(e.getMessage());
            response.setValid(false);
        } catch (Exception e) {
            response.setMessage("Error al guardar el carrito: " + e.getMessage());
            response.setValid(false);
        }
        return response;
    }

    public CarritoCompraPlanReponse obtenerCarritosPorCliente(Long clienteId) {
        CarritoCompraPlanReponse response = new CarritoCompraPlanReponse();
        try {
            List<CarritoCompraPlanDTO> carritosList = carritoRepository.findByClienteIdCliente(clienteId)
                    .stream()
                    .filter(plan -> !plan.isEliminado())
                    .map(CarritoCompraPlanMapper::toDTO)
                    .collect(Collectors.toList());

            response.setCarritosList(carritosList);
            if (carritosList.isEmpty()) {
                response.setMessage("El cliente no tiene planes en el carrito");
            } else {
                response.setMessage("Carritos obtenidos para el cliente con ID " + clienteId);
            }
            response.setValid(true);
        } catch (Exception e) {
            response.setMessage("Error al obtener los carritos del cliente: " + e.getMessage());
            response.setValid(false);
        }
        return response;
    }

    public CarritoCompraPlanReponse obtenerCarritos() {
        CarritoCompraPlanReponse response = new CarritoCompraPlanReponse();
        try {
            List<CarritoCompraPlanDTO> carritosList = carritoRepository.findAll()
                    .stream()
                    .filter(plan -> !plan.isEliminado())
                    .map(CarritoCompraPlanMapper::toDTO)
                    .collect(Collectors.toList());

            response.setCarritosList(carritosList);
            response.setMessage("Lista de carritos obtenida.");
            response.setValid(true);
        } catch (Exception e) {
            response.setMessage("Error al obtener los carritos: " + e.getMessage());
            response.setValid(false);
        }
        return response;
    }

    public CarritoCompraPlanReponse eliminarCompra(Long id) {

        CarritoCompraPlanReponse response = new CarritoCompraPlanReponse();

        try {

            Optional<CarritoCompraPlan> compra = carritoRepository.findById(id);

            if (compra.isPresent()) {
                compra.get().setEliminado(true);
                response.setMessage("Eliminaste este producto del carrito de compra");
                response.setValid(true);

                carritoRepository.save(compra.get());
            } else {
                response.setMessage("No se encontro este producto en el carrito de compra");
                response.setValid(true);
            }

        } catch (Exception e) {
            response.setMessage("Error al eliminar la compra: " + e.getMessage());
            response.setValid(false);
        }

        return response;
    }
}
