package com.app.demo.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.app.demo.domain.dto.CompraPlanDTO;
import com.app.demo.domain.dto.PagoDatosDTO;
import com.app.demo.domain.mapper.CompraPlanMapper;
import com.app.demo.domain.response.PagoDatosResponse;
import com.app.demo.persistence.entity.CompraPlan;
import com.app.demo.persistence.repository.CompraPlanRepository;

@Service
public class PagoService {

    private static final String MONEDA_COP = "COP";

    private final CompraPlanRepository compraPlanRepository;

    // Credenciales de PayU
    @Value("${payu.api-key:}")
    private String payuApiKey;

    @Value("${payu.merchant-id:}")
    private String payuMerchantId;

    @Value("${payu.account-id:}")
    private String payuAccountId;

    public PagoService(CompraPlanRepository compraPlanRepository) {
        this.compraPlanRepository = compraPlanRepository;
    }

    public PagoDatosResponse crearDatosPago(CompraPlanDTO compraPlanDTO) {
        // Validaciones de seguridad para tu proyecto "My Trip"
        if (compraPlanDTO == null || compraPlanDTO.getCliente() == null || compraPlanDTO.getCliente().getIdCliente() == null) {
            return new PagoDatosResponse("Solicitud de pago inválida", false, null);
        }

        if (compraPlanDTO.getPrecioTotalCompra() == null || compraPlanDTO.getPrecioTotalCompra().compareTo(BigDecimal.ZERO) <= 0) {
            return new PagoDatosResponse("El monto debe ser mayor a 0", false, null);
        }

        if (payuApiKey.isBlank() || payuMerchantId.isBlank()) {
            return new PagoDatosResponse("Faltan credenciales de PayU en el backend", false, null);
        }

        // 1. Preparar datos de PayU
        String referencia = generarReferencia(compraPlanDTO);
        // PayU maneja el monto como decimal. Ej: "50000.00"
        BigDecimal monto = compraPlanDTO.getPrecioTotalCompra().setScale(2, RoundingMode.HALF_UP);
        String montoStr = monto.toString();
        
        // 2. Generar Firma PayU: ApiKey~merchantId~referenceCode~amount~currency
        String firma = generarFirmaPayU(referencia, montoStr, MONEDA_COP);

        // 3. Guardar persistencia antes de ir a la pasarela
        CompraPlan compraPendiente = CompraPlanMapper.toEntity(compraPlanDTO);
        compraPendiente.setFechaCompra(LocalDate.now().toString());
        compraPendiente.setEstado("PENDIENTE_PAGO");
        compraPendiente.setReferenciaPago(referencia);
        compraPlanRepository.save(compraPendiente);

        // 4. Mapear al DTO que recibirá el Frontend (React)
        PagoDatosDTO pagoDatos = PagoDatosDTO.builder()
                .referencia(referencia)
                .monto(montoStr)
                .moneda(MONEDA_COP)
                .firma(firma)
                .merchantId(payuMerchantId)
                .accountId(payuAccountId)
                .descripcion("Compra de Plan Turístico - My Trip")
                .build();

        return new PagoDatosResponse("Datos de pago PayU generados", true, pagoDatos);
    }

    /**
     * Algoritmo de firma para PayU: ApiKey~merchantId~referenceCode~amount~currency
     * PayU recomienda usar SHA-256 o MD5.
     */
    public String generarFirmaPayU(String referencia, String monto, String moneda) {
        String payload = payuApiKey + "~" + payuMerchantId + "~" + referencia + "~" + monto + "~" + moneda;
        return sha256Hex(payload);
    }

    private String generarReferencia(CompraPlanDTO compraPlanDTO) {
        String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        return "MT-" + compraPlanDTO.getCliente().getIdCliente() + "-" + timestamp;
    }

    // El Webhook de PayU envía parámetros diferentes (ej: state_pol)
    public boolean actualizarEstadoPorPayU(Map<String, String> params) {
        String referencia = params.get("reference_sale");
        String estadoPayU = params.get("state_pol"); // 4 = Aprobado, 6 = Rechazado
        String idTransaccion = params.get("transaction_id");

        Optional<CompraPlan> compraOptional = compraPlanRepository.findByReferenciaPago(referencia);
        if (compraOptional.isPresent()) {
            CompraPlan compra = compraOptional.get();
            compra.setEstado(mapearEstadoPayU(estadoPayU));
            compra.setIdTransaccionPago(idTransaccion);
            compraPlanRepository.save(compra);
            return true;
        }
        return false;
    }

    private String mapearEstadoPayU(String statePol) {
        return switch (statePol) {
            case "4" -> "PAGADO";
            case "6" -> "RECHAZADO";
            case "5" -> "ANULADO";
            default -> "PENDIENTE_PAGO";
        };
    }

    private static String sha256Hex(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            for (byte b : hash) {
                result.append(String.format("%02x", b));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Error al generar firma", ex);
        }
    }
}