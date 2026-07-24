package com.iexpo.marketplace.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test/ai")
public class AiController {

    private static final Logger log = LoggerFactory.getLogger(AiController.class);
    private final ChatClient openAiChatClient;
    private final ChatClient googleGenAiChatClient;
    private final ChatClient anthropicChatClient;
    private final ChatClient ollamaChatClient;

    public AiController(@Qualifier("openAiChatClient") ChatClient openAiChatClient,
            @Qualifier("googleGenAiChatClient") ChatClient googleGenAiChatClient,
            @Qualifier("anthropicChatClient") ChatClient anthropicChatClient,
                        @Qualifier("ollamaChatClient") ChatClient ollamaChatClient) {
        this.openAiChatClient = openAiChatClient;
        this.googleGenAiChatClient = googleGenAiChatClient;
        this.anthropicChatClient = anthropicChatClient;
        this.ollamaChatClient = ollamaChatClient;
    }

    @GetMapping("/openai")
    public ResponseEntity<Map<String, Object>> promptOpenAi(
            @RequestParam(value = "prompt", defaultValue = "Hello, tell me a quick joke about AI.") String prompt,
            @RequestParam(value = "conversationId", defaultValue = "default") String conversationId) {
        Map<String, Object> response = new HashMap<>();
        response.put("provider", "OpenAI");
        response.put("prompt", prompt);
        response.put("conversationId", conversationId);

        log.info("OpenAI ...........................");
        try {
            String result = openAiChatClient.prompt()
                    .user(prompt)
                    .advisors(a -> a.param("chat_memory_conversation_id", conversationId))
                    .call()
                    .content();
            response.put("status", "success");
            response.put("response", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to get response from OpenAI. Ensure OPENAI_API_KEY is configured and valid.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/gemini")
    public ResponseEntity<Map<String, Object>> promptGemini(
            @RequestParam(value = "prompt", defaultValue = "Hello, tell me a quick joke about AI.") String prompt,
            @RequestParam(value = "conversationId", defaultValue = "default") String conversationId) {
        Map<String, Object> response = new HashMap<>();
        response.put("provider", "Google Gemini");
        response.put("prompt", prompt);
        response.put("conversationId", conversationId);
        log.info("Gemini Claude ...........................");
        try {
            String result = googleGenAiChatClient.prompt()
                    .user(prompt)
                    .advisors(a -> a.param("chat_memory_conversation_id", conversationId))
                    .call()
                    .content();
            response.put("status", "success");
            response.put("response", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to get response from Google Gemini. Ensure GOOGLE_GENAI_API_KEY is configured and valid.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/claude")
    public ResponseEntity<Map<String, Object>> promptClaude(
            @RequestParam(value = "prompt", defaultValue = "Hello, tell me a quick joke about AI.") String prompt,
            @RequestParam(value = "conversationId", defaultValue = "default") String conversationId) {
        Map<String, Object> response = new HashMap<>();
        response.put("provider", "Anthropic Claude");
        response.put("prompt", prompt);
        response.put("conversationId", conversationId);
        log.info("Anthropic Claude ...........................");
        try {
            String result = anthropicChatClient.prompt()
                    .user(prompt)
                    .advisors(a -> a.param("chat_memory_conversation_id", conversationId))
                    .call()
                    .content();
            response.put("status", "success");
            response.put("response", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to get response from Anthropic Claude. Ensure ANTHROPIC_API_KEY is configured and valid.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/ollama")
    public ResponseEntity<Map<String, Object>> promptOllama(
            @RequestParam(value = "prompt", defaultValue = "Hello, tell me a quick joke about AI.") String prompt,
            @RequestParam(value = "conversationId", defaultValue = "default") String conversationId) {
        Map<String, Object> response = new HashMap<>();
        response.put("provider", "Ollama AI");
        response.put("prompt", prompt);
        response.put("conversationId", conversationId);
        log.info("Ollama AI ...............................");
        try {
            String result = ollamaChatClient.prompt()
                    .user(prompt)
                    .advisors(a -> a.param("chat_memory_conversation_id", conversationId))
                    .call()
                    .content();
            response.put("status", "success");
            response.put("response", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to get response from Anthropic Claude. Ensure ANTHROPIC_API_KEY is configured and valid.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
