package com.app.demo.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    @Value("${payu.api-key:4Vj8eK4rloUd272L48hsrarnUA}")
    private String payuApiKey;

    @Value("${payu.merchant-id:508029}")
    private String payuMerchantId;

    @Value("${payu.account-id:512321}")
    private String payuAccountId;

    /**
     * Recibe el monto y devuelve la firma MD5 para PayU Sandbox
     * junto con los datos necesarios para iniciar el formulario de pago.
     */
    @PostMapping("/firma")
    public ResponseEntity<Map<String, Object>> generarFirma(@RequestBody FirmaRequest request) {
        if (request.getMonto() == null || request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("valid", false, "message", "El monto debe ser mayor a 0"));
        }

        String referencia = "MT-" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        String moneda = (request.getMoneda() != null && !request.getMoneda().isBlank())
                ? request.getMoneda() : "COP";
        String montoStr = request.getMonto().setScale(2, RoundingMode.HALF_UP).toPlainString();

        // Algoritmo MD5 requerido por PayU Sandbox: ApiKey~merchantId~referenceCode~amount~currency
        String payload = payuApiKey + "~" + payuMerchantId + "~" + referencia + "~" + montoStr + "~" + moneda;
        String firma = md5Hex(payload);

        return ResponseEntity.ok(Map.of(
                "valid", true,
                "pagoDatos", Map.of(
                        "urlPayU", "https://sandbox.checkout.payulatam.com/ppp-web-gateway-payu/",
                        "firma", firma,
                        "referencia", referencia,
                        "monto", montoStr,
                        "moneda", moneda,
                        "merchantId", payuMerchantId,
                        "accountId", payuAccountId,
                        "descripcion", "Compra de Plan Turístico - My Trip"
                )
        ));
    }

    private static String md5Hex(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Error al generar firma MD5", ex);
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestBody(required = false) String body) {
        // PayU calls this server-to-server to confirm payments.
        // For production, verify the signature before processing.
        return ResponseEntity.ok("OK");
    }

    public static class FirmaRequest {
        private BigDecimal monto;
        private String moneda;

        public BigDecimal getMonto() { return monto; }
        public void setMonto(BigDecimal monto) { this.monto = monto; }
        public String getMoneda() { return moneda; }
        public void setMoneda(String moneda) { this.moneda = moneda; }
    }
}
