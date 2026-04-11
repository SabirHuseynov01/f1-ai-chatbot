package com.example.f1aichatbot.service;

import com.example.f1aichatbot.model.ChatMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OllamaApiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${ollama.api.url:http://localhost:11434}")
    private String ollamaUrl;

    @Value("${ollama.api.model:llama3.2}")
    private String model;

    public OllamaApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final String F1_SYSTEM_PROMPT = """
            You are an AI assistant specializing in Formula 1. Your name is "F1 Intelligence".
            
            Your areas of expertise include:
            - F1 drivers: career statistics, histories, achievements
            - F1 teams: history, technical information, championships
            - Race calendars and results
            - F1 history: all major events from 1950 to the present
            - Technical information: DRS, pit stop strategies, aerodynamics, engine technologies
            - Current season information
            
            Your rules:
            - Always respond in Turkish (unless the user uses another language)
            - Speak like an enthusiastic, knowledgeable, and passionate F1 commentator
            - Divide your answers into clear sections, highlighting important information
            - Start your answers with "To my knowledge..." if you are unsure about something
            - Gently redirect the conversation back to F1 if it goes off-topic 
            - Respond using information from the given context data
            """;

    public String sendMessage(String userMessage, List<ChatMessage> chatHistory, String f1Context) {
        try {
            String url = ollamaUrl + "/api/chat";

            // Create a message list.
            List<Map<String, String>> messages = new ArrayList<>();

            // System prompt
            messages.add(Map.of("role", "system", "content", F1_SYSTEM_PROMPT));

            // Past messages
            if (chatHistory != null && !chatHistory.isEmpty()) {
                for (ChatMessage msg : chatHistory) {
                    if (msg.getRole() != ChatMessage.MessageRole.SYSTEM) {
                        String role = msg.getRole() == ChatMessage.MessageRole.USER ? "user" : "assistant";
                        messages.add(Map.of("role", role, "content", msg.getContent()));
                    }
                }
            }

            // Current message + F1 context
            String enrichedMessage = userMessage;
            if (f1Context != null && !f1Context.isBlank()) {
                enrichedMessage = userMessage + "\n\n[Database Information]\n" + f1Context;
            }
            messages.add(Map.of("role", "user", "content", enrichedMessage));

            // Request body
            Map<String, Object> request = Map.of(
                    "model", model,
                    "messages", messages,
                    "stream", false
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            log.debug("A request is being sent to the Ollama API.. Model: {}", model);

            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            String text = root.path("message").path("content").asText();

            log.debug("Ollama's response was received..");
            return text;

        } catch (Exception e) {
            log.error("Ollama API hatası: {}", e.getMessage(), e);
            throw new RuntimeException("Could not connect to the Ollama service. Is Ollama working? Kontrol: http://localhost:11434 — Error: " + e.getMessage());
        }
    }
}


