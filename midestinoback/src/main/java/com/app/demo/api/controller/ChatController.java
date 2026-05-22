package com.app.demo.api.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.domain.dto.ChatRequestDTO;
import com.app.demo.domain.response.ChatResponse;
import com.app.demo.domain.service.TravelChatService;
import com.app.demo.integration.google.groq.GroqClient;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final TravelChatService travelChatService;
    private final GroqClient groqClient;

    public ChatController(TravelChatService travelChatService, GroqClient groqClient) {
        this.travelChatService = travelChatService;
        this.groqClient = groqClient;
    }

    @PostMapping("/itinerario")
    public ResponseEntity<ChatResponse> generarItinerario(@RequestBody ChatRequestDTO request) {
        ChatResponse response = travelChatService.generarRespuesta(request);
        HttpStatus status = response.isValid() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> testGroq() {
        String resultado = groqClient.generateResponse("Responde solo: 'OK'");
        boolean ok = !resultado.startsWith("Error");
        return ResponseEntity.ok(Map.of(
            "status", ok ? "ok" : "error",
            "respuesta", resultado
        ));
    }
}
