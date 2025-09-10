package com.forwork.backend.api.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FindIdResponseDTO {
    private String userId;
    private LocalDateTime createdAt;
}
