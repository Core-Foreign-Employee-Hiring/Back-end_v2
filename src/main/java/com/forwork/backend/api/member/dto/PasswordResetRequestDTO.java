package com.forwork.backend.api.member.dto;

import lombok.Getter;

@Getter
public class PasswordResetRequestDTO {

    private String userId;
    private String name;
    private String email;
}