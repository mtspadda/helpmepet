package com.padda.helpmepet.service;

import com.padda.helpmepet.dto.OllamaRequest;
import com.padda.helpmepet.dto.OllamaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OllamaService {

    private final WebClient ollamaWebClient;

    @Value("${ollama.model:gemma3}")
    private String model;

    @Value("${ollama.timeout:60}")
    private int timeoutSeconds;

    public String generate(List<OllamaRequest.OllamaMessage> messages) {
        OllamaRequest request = OllamaRequest.builder()
                .model(model)
                .messages(messages)
                .stream(false)
                .options(OllamaRequest.OllamaOptions.builder()
                        .temperature(0.7f)
                        .numPredict(1024)
                        .build())
                .build();

        log.debug("Enviando {} mensagens para Ollama (modelo: {})", messages.size(), model);

        OllamaResponse response = ollamaWebClient.post()
                .uri("/api/chat")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OllamaResponse.class)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .doOnError(e -> log.error("Erro ao chamar Ollama: {}", e.getMessage()))
                .block();

        if (response == null || response.getMessage() == null) {
            throw new RuntimeException("Resposta inválida do modelo");
        }

        return response.getMessage().getContent();
    }
}
