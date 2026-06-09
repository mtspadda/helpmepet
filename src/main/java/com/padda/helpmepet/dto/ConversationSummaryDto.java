package com.padda.helpmepet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ConversationSummaryDto {
    private Long id;
    private String title;
    private LocalDateTime updatedAt;
}
