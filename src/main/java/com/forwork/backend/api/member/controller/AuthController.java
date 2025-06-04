package com.forwork.backend.api.member.controller;

import com.forwork.backend.api.member.dto.EmailVerificationRequestDTO;
import com.forwork.backend.api.member.dto.SmsVerificationRequestDTO;
import com.forwork.backend.api.member.dto.EmailVerificationCodeRequestDTO;
import com.forwork.backend.api.member.dto.SmsVerificationCodeRequestDTO;
import com.forwork.backend.api.member.service.EmailService;
import com.forwork.backend.api.member.service.SmsService;
import com.forwork.backend.common.exception.BadRequestException;
import com.forwork.backend.common.response.ApiResponse;
import com.forwork.backend.common.response.ErrorStatus;
import com.forwork.backend.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Tag(name = "Member", description = "Member 관련 API 입니다.")
@RestController
@RequestMapping("/api/v2/member")
@RequiredArgsConstructor
public class AuthController {

    private final EmailService emailService;
    private final SmsService smsService;

    @Operation(
            summary = "이메일 인증코드 발송 API (태근)",
            description = "이메일 인증코드를 발송합니다.<br>"
                    + "<p>"
                    + "호출 필드 정보) <br>"
                    + "email : 사용자 이메일"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이메일 인증코드 발송 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "올바른 이메일 형식이 아닙니다."),
    })
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> getEmailVerification(@RequestBody EmailVerificationRequestDTO emailVerificationRequestDTO) {
        LocalDateTime requestedAt = LocalDateTime.now();
        String email = emailVerificationRequestDTO.getEmail();

        // Apache Commons EmailValidator 검증
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new BadRequestException(ErrorStatus.VALIDATION_EMAIL_FORMAT_EXCEPTION.getMessage());
        }

        emailService.sendVerificationEmail(email, requestedAt);
        return ApiResponse.success_only(SuccessStatus.SEND_EMAIL_VERIFICATION_CODE_SUCCESS);
    }

    @Operation(
            summary = "이메일 코드 인증 API (태근)",
            description = "발송된 이메일 인증 코드를 검증합니다.<br>"
                    + "<p>"
                    + "호출 필드 정보) <br>"
                    + "code : 이메일로 발송된 인증코드"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이메일 코드 인증 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "이메일 인증코드가 올바르지 않습니다."),
    })
    @PostMapping("/verification-email-code")
    public ResponseEntity<ApiResponse<Void>> verificationByCode(@RequestBody EmailVerificationCodeRequestDTO emailVerificationCodeRequestDTO) {
        LocalDateTime requestedAt = LocalDateTime.now();
        emailService.verifyEmail(emailVerificationCodeRequestDTO.getCode(), requestedAt);
        return ApiResponse.success_only(SuccessStatus.SEND_EMAIL_VERIFICATION_SUCCESS);
    }

    @Operation(
            summary = "SMS 인증코드 발송 API (태근)",
            description = "휴대폰으로 인증코드를 발송합니다.<br>"
                    + "<p>"
                    + "호출 필드 정보) <br>"
                    + "phoneNumber : 전화번호 (예시 : 01012345678)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "SMS 인증코드 발송 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "휴대폰 번호 형식이 올바르지 않습니다."),
    })
    @PostMapping("/verify-phone")
    public ResponseEntity<ApiResponse<Void>> sendVerificationSms(@RequestBody SmsVerificationRequestDTO smsVerificationRequestDTO) {
        String phoneNumber = smsVerificationRequestDTO.getPhoneNumber();
        LocalDateTime requestedAt = LocalDateTime.now();

        if (StringUtils.isBlank(phoneNumber) || !phoneNumber.matches("\\d{10,11}")) {
            throw new BadRequestException(ErrorStatus.VALIDATION_PHONE_FORMAT_EXCEPTION.getMessage());
        }

        smsService.sendVerificationSms(phoneNumber, requestedAt);
        return ApiResponse.success_only(SuccessStatus.SEND_SMS_VERIFICATION_CODE_SUCCESS);
    }

    @Operation(
            summary = "SMS 코드 인증 API (태근)",
            description = "발송된 SMS 인증 코드를 검증합니다.<br>"
                    + "<p>"
                    + "호출 필드 정보) <br>"
                    + "code : 문자로 발송된 인증코드"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "SMS 코드 인증 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "SMS 인증코드가 올바르지 않습니다."),
    })
    @PostMapping("/verification-phone-code")
    public ResponseEntity<ApiResponse<Void>> verifyPhoneCode(@RequestBody SmsVerificationCodeRequestDTO smsVerificationCodeRequestDTO) {
        LocalDateTime requestedAt = LocalDateTime.now();
        smsService.verifyCode(smsVerificationCodeRequestDTO.getCode(), requestedAt);
        return ApiResponse.success_only(SuccessStatus.SEND_VERIFY_SMS_CODE_SUCCESS);
    }

}