package com.forwork.backend.api.member.dto;

import lombok.Getter;

@Getter
public class PasswordResetConfirmDTO {

    private String code;
    private String newPassword;
}
