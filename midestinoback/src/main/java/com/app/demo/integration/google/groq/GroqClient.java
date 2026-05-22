package com.app.demo.integration.google.groq;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GroqClient {

    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String model;

    public GroqClient(RestTemplateBuilder builder, 
                      @Value("${groq.api-key}") String apiKey,
                      @Value("${groq.model}") String model) {
        this.restTemplate = builder.build();
        this.apiKey = apiKey != null ? apiKey.trim() : "";
        this.model = model != null ? model.trim() : "llama-3.3-70b-versatile";
    }

    public String chat(String systemInstructions, String userPrompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Línea 32: Solución definitiva a Null type safety para apiKey
            headers.setBearerAuth(Objects.requireNonNullElse(apiKey, ""));

            Map<String, Object> body = Map.of(
                "model", Objects.requireNonNullElse(this.model, "llama-3.1-8b-instant"), 
                "messages", List.of(
                    Map.of("role", "system", "content", systemInstructions),
                    Map.of("role", "user", "content", userPrompt)
                ),
                "temperature", 0.7
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            
            // Línea 48: Forzamos que HttpMethod no sea nulo para el compilador
            HttpMethod method = Objects.requireNonNull(HttpMethod.POST);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                API_URL, 
                method, 
                entity, 
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null) return "Error: Respuesta vacía";

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            
            if (choices == null || choices.isEmpty()) return "Error: Sin opciones";

            Map<String, Object> firstChoice = choices.get(0);
            @SuppressWarnings("unchecked")
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
            
            return message != null ? (String) message.get("content") : "Error: Contenido nulo";

        } catch (org.springframework.web.client.RestClientException e) {
            // Solución al aviso de multicatch: capturamos excepciones específicas
            return "Error de red en Groq: " + e.getMessage();
        } catch (Exception e) {
            return "Error inesperado: " + e.getMessage();
        }
    }

    public String generateResponse(String userPrompt) {
        String systemInstructions = "Eres un asistente de viajes experto especializado en destinos turísticos de Colombia.";
        return chat(systemInstructions, userPrompt);
    }
}
