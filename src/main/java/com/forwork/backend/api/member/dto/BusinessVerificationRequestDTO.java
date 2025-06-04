package com.forwork.backend.api.member.dto;

import lombok.Getter;

@Getter
public class BusinessVerificationRequestDTO {
    private String businessNo;
    private String startDate;
    private String representativeName;
}