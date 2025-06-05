package com.forwork.backend.api.member.dto;

import lombok.Getter;

@Getter
public class AgreementRequestDTO {
    private boolean termsOfServiceAgreement;
    private boolean over15;
    private boolean personalInfoAgreement;
    private boolean adInfoAgreementSmsMms;
    private boolean adInfoAgreementEmail;
}