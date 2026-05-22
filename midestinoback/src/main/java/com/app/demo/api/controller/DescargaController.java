package com.app.demo.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/descarga")
public class DescargaController {

    @Value("${app.apk.url:}")
    private String apkUrl;

    @GetMapping("/apk")
    public ResponseEntity<?> descargarApk() throws Exception {
        // Si hay una URL externa configurada (producción), redirige allí
        if (apkUrl != null && !apkUrl.isBlank()) {
            return ResponseEntity.status(302)
                    .location(URI.create(apkUrl))
                    .build();
        }

        // Fallback: sirve el archivo local (desarrollo)
        Resource resource = new ClassPathResource("static/apk/MiDestino.apk");
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"MiDestino.apk\"")
                .contentType(MediaType.parseMediaType("application/vnd.android.package-archive"))
                .contentLength(resource.contentLength())
                .body(resource);
    }
}
