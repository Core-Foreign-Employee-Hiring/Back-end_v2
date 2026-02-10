package com.forwork.backend.api.resume.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResumeCreateRequest {
    @NotBlank(message = "이력서 이름은 필수입니다")
    private String resumeName;

    @NotBlank(message = "템플릿은 필수입니다")
    private String template;

    private String introduction;

    @Valid
    private List<ResumeUrlDto> urls;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResumeUrlDto {
        private String urlTitle;

        private String urlLink;
    }
}
