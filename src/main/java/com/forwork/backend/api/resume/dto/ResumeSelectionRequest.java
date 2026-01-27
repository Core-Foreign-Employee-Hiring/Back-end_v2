package com.forwork.backend.api.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResumeSelectionRequest {
    private boolean includeIntroduction;
    private boolean includeEducation;
    private boolean includeCertificate;
    private boolean includeLanguage;
    private boolean includeCareer;
    private boolean includeAward;
    private boolean includeActivity;
    private boolean includeUrls;
}
