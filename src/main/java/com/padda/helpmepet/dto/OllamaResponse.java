package com.padda.helpmepet.dto;

import lombok.Data;

@Data
public class OllamaResponse {
    private OllamaRequest.OllamaMessage message;
    private boolean done;
    private String model;
}
