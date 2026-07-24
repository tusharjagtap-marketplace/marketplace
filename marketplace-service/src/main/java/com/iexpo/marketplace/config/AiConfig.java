package com.iexpo.marketplace.config;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)
                .build();
    }

    @Bean
    public MessageChatMemoryAdvisor messageChatMemoryAdvisor(ChatMemory chatMemory) {
        return MessageChatMemoryAdvisor.builder(chatMemory).build();
    }

    @Bean
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel, MessageChatMemoryAdvisor advisor) {
        return ChatClient.builder(openAiChatModel)
                .defaultAdvisors(advisor)
                .build();
    }

    @Bean
    public ChatClient googleGenAiChatClient(GoogleGenAiChatModel googleGenAiChatModel, MessageChatMemoryAdvisor advisor) {
        return ChatClient.builder(googleGenAiChatModel)
                .defaultAdvisors(advisor)
                .build();
    }

    @Bean
    public ChatClient anthropicChatClient(AnthropicChatModel anthropicChatModel, MessageChatMemoryAdvisor advisor) {
        return ChatClient.builder(anthropicChatModel)
                .defaultAdvisors(advisor)
                .build();
    }

    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel, MessageChatMemoryAdvisor advisor) {
        return ChatClient.builder(ollamaChatModel)
                .defaultAdvisors(advisor)
                .build();
    }
}
