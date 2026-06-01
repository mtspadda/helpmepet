package com.padda.helpmepet.config;

import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OllamaConfig {

    @Bean
    public String systemPrompt(){
        return """
            Você é um assistente especializado em adoção de animais
            abandonados e primeiros cuidados com pets resgatados.
            Responda sempre em português, com empatia e clareza.
            Foque em: processo de adoção, vacinação inicial,
            vermifugação, alimentação, socialização e veterinários.
            Se a dúvida estiver fora desse escopo, redirecione
            gentilmente ao tema.
            """;
    }

    @Bean
    public WebClient ollamaClient(@Value("${ollama.base-url}") String url) {
        return WebClient.builder().baseUrl(url).build();
    }
}
