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

    private String introduction;

    @Valid
    private List<ResumeUrlDto> urls;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResumeUrlDto {
        @NotBlank(message = "URL 제목은 필수입니다")
        private String urlTitle;

        @NotBlank(message = "URL 링크는 필수입니다")
        private String urlLink;
    }
}
