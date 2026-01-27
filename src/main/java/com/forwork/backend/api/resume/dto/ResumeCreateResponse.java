package com.forwork.backend.api.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeCreateResponse {
    private Long resumeId;
    private String resumeName;
}
