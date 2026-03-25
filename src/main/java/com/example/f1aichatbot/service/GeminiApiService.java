package com.example.f1aichatbot.service;

import com.example.f1aichatbot.config.GeminiConfig;
import com.example.f1aichatbot.model.ChatMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiApiService {

    private final RestTemplate restTemplate;
    private final GeminiConfig config;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s";

    private static final String F1_SYSTEM_PROMPT = """
            Sen bir Formula 1 uzmanı yapay zeka asistanısın. Adın "F1 Intelligence".

            Uzmanlık alanların:
            - F1 sürücüleri: kariyer istatistikleri, geçmişleri, başarıları
            - F1 takımları: tarihçe, teknik bilgiler, şampiyonluklar
            - Yarış takvimleri ve sonuçları
            - F1 tarihi: 1950'den günümüze tüm önemli olaylar
            - Teknik bilgiler: DRS, pit stop stratejileri, aerodinamik, motor teknolojileri
            - Güncel sezon bilgileri

            Kuralların:
            - Her zaman Türkçe yanıt ver (kullanıcı başka dil kullanmadıkça)
            - Heyecanlı, bilgili ve tutkulu bir F1 yorumcusu gibi konuş
            - Yanıtlarını net bölümlere ayır, önemli bilgileri vurgula
            - Emin olmadığın bilgileri "Bildiğim kadarıyla..." diye başlat
            - Konu F1 dışına çıkarsa nazikçe F1'e geri yönlendir
            - Verilen context verisindeki bilgileri kullanarak yanıtla
            """;

    public String sendMessage(String userMessage, List<ChatMessage> chatHistory, String f1Context) {
        try {
            String url = String.format(GEMINI_API_URL, config.getModel(), config.getKey());

            // Build conversation
            List<Map<String, Object>> contents = new ArrayList<>();

            // Add system prompt as first user message
            contents.add(Map.of(
                "role", "user",
                "parts", List.of(Map.of("text", F1_SYSTEM_PROMPT))
            ));
            contents.add(Map.of(
                "role", "model",
                "parts", List.of(Map.of("text", "Anladım! F1 uzmanı olarak sorularınıza yardımcı olmaya hazırım."))
            ));

            // Add chat history
            if (chatHistory != null && !chatHistory.isEmpty()) {
                for (ChatMessage msg : chatHistory) {
                    if (msg.getRole() != ChatMessage.MessageRole.SYSTEM) {
                        String role = msg.getRole() == ChatMessage.MessageRole.USER ? "user" : "model";
                        contents.add(Map.of(
                            "role", role,
                            "parts", List.of(Map.of("text", msg.getContent()))
                        ));
                    }
                }
            }

            // Add current message with F1 context
            String enrichedMessage = userMessage;
            if (f1Context != null && !f1Context.isBlank()) {
                enrichedMessage = userMessage + "\n\n[Veritabanı Bilgileri]\n" + f1Context;
            }

            contents.add(Map.of(
                "role", "user",
                "parts", List.of(Map.of("text", enrichedMessage))
            ));

            // Build request
            Map<String, Object> request = Map.of(
                "contents", contents,
                "generationConfig", Map.of(
                    "maxOutputTokens", config.getMaxTokens(),
                    "temperature", 0.7
                )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            log.debug("Gemini API'ye istek gönderiliyor. Model: {}", config.getModel());

            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
            );

            // Parse response
            JsonNode root = objectMapper.readTree(response.getBody());
            String text = root.path("candidates").get(0)
                .path("content").path("parts").get(0)
                .path("text").asText();

            log.debug("Gemini yanıtı alındı.");
            return text;

        } catch (HttpClientErrorException e) {
            log.error("Gemini API HTTP hatası: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("API hatası: " + e.getStatusCode() + " - " + e.getMessage());
        } catch (Exception e) {
            log.error("Gemini API'ye bağlanırken hata: {}", e.getMessage(), e);
            throw new RuntimeException("AI servisine bağlanılamadı: " + e.getMessage());
        }
    }
}
