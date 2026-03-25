package com.example.f1aichatbot.service;

import com.example.f1aichatbot.dto.ChatDTOs;
import com.example.f1aichatbot.model.ChatMessage;
import com.example.f1aichatbot.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final GeminiApiService geminiApiService;
    private final F1DataService f1DataService;

    @Value("${chat.max-history-size:20}")
    private int maxHistorySize;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    // ===========================
    // Ana Chat İşlemi
    // ===========================

    @Transactional
    public ChatDTOs.ChatResponse processMessage(ChatDTOs.ChatRequest request) {
        // Session ID yoksa yeni oluştur
        String sessionId = (request.getSessionId() != null && !request.getSessionId().isBlank())
                ? request.getSessionId()
                : generateSessionId();

        log.info("Mesaj işleniyor. Session: {}, Mesaj: {}",
                sessionId, request.getMessage().substring(0, Math.min(50, request.getMessage().length())));

        try {
            // 1. Kullanıcı mesajını kaydet
            saveMessage(sessionId, ChatMessage.MessageRole.USER, request.getMessage());

            // 2. Sohbet geçmişini çek (son N mesaj)
            List<ChatMessage> history = getRecentHistory(sessionId);

            // 3. Soruya uygun F1 verisini çek
            String f1Context = f1DataService.buildRelevantContext(request.getMessage());
            log.debug("F1 Context uzunluğu: {} karakter", f1Context.length());

            // 4. Gemini API'ye gönder
            String assistantMessage = geminiApiService.sendMessage(
                    request.getMessage(), history, f1Context);

            // 5. Asistan yanıtını kaydet
            saveMessage(sessionId, ChatMessage.MessageRole.ASSISTANT, assistantMessage);

            return ChatDTOs.ChatResponse.builder()
                    .message(assistantMessage)
                    .sessionId(sessionId)
                    .success(true)
                    .build();

        } catch (Exception e) {
            log.error("Mesaj işlenirken hata: {}", e.getMessage(), e);

            // Hata mesajını da kaydet
            String errorMsg = "Üzgünüm, şu an yanıt veremiyorum. Lütfen tekrar deneyin.";
            saveMessage(sessionId, ChatMessage.MessageRole.ASSISTANT, errorMsg);

            return ChatDTOs.ChatResponse.builder()
                    .message(errorMsg)
                    .sessionId(sessionId)
                    .success(false)
                    .error(e.getMessage())
                    .build();
        }
    }

    // ===========================
    // Sohbet Geçmişi
    // ===========================

    public ChatDTOs.ChatHistoryResponse getHistory(String sessionId) {
        List<ChatMessage> messages = chatMessageRepository
                .findBySessionIdOrderByCreatedAtAsc(sessionId);

        List<ChatDTOs.MessageDTO> messageDTOs = messages.stream()
                .map(m -> ChatDTOs.MessageDTO.builder()
                        .role(m.getRole().name().toLowerCase())
                        .content(m.getContent())
                        .timestamp(m.getCreatedAt() != null
                                ? m.getCreatedAt().format(FORMATTER) : "")
                        .build())
                .collect(Collectors.toList());

        return ChatDTOs.ChatHistoryResponse.builder()
                .sessionId(sessionId)
                .messages(messageDTOs)
                .totalMessages(messageDTOs.size())
                .build();
    }

    @Transactional
    public void clearHistory(String sessionId) {
        chatMessageRepository.deleteBySessionId(sessionId);
        log.info("Session temizlendi: {}", sessionId);
    }

    // ===========================
    // Yardımcı Metodlar
    // ===========================

    private List<ChatMessage> getRecentHistory(String sessionId) {
        return chatMessageRepository.findRecentBySessionId(
                sessionId,
                PageRequest.of(0, maxHistorySize)
        ).reversed(); // Eskiden yeniye sırala
    }

    private void saveMessage(String sessionId, ChatMessage.MessageRole role, String content) {
        saveMessageWithTokens(sessionId, role, content, null);
    }

    private void saveMessageWithTokens(String sessionId, ChatMessage.MessageRole role,
                                       String content, Integer tokens) {
        ChatMessage message = ChatMessage.builder()
                .sessionId(sessionId)
                .role(role)
                .content(content)
                .tokensUsed(tokens)
                .build();
        chatMessageRepository.save(message);
    }

    private String generateSessionId() {
        return "session-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
