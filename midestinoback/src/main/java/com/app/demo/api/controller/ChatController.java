package com.app.demo.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.domain.dto.ChatRequestDTO;
import com.app.demo.domain.response.ChatResponse;
import com.app.demo.domain.service.TravelChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final TravelChatService travelChatService;

    public ChatController(TravelChatService travelChatService) {
        this.travelChatService = travelChatService;
    }

    @PostMapping("/itinerario")
    public ResponseEntity<ChatResponse> generarItinerario(@RequestBody ChatRequestDTO request) {
        ChatResponse response = travelChatService.generarRespuesta(request);
        HttpStatus status = response.isValid() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
}
