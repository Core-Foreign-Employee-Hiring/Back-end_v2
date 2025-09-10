package com.forwork.backend.api.member.service;

import com.forwork.backend.api.member.entity.PhoneNumberVerification;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.api.member.repository.PhoneNumberVerificationRepository;
import com.forwork.backend.common.exception.InternalServerException;
import com.forwork.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final PhoneNumberVerificationRepository phoneNumberVerificationRepository;
    private final MemberRepository memberRepository;

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.api.number}")
    private String senderPhoneNumber;

    private DefaultMessageService messageService;

    public void initializeMessageService() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    //  가입/변경용
    public void sendVerificationSms(String phoneNumber, LocalDateTime requestedAt) {
        if (memberRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new com.forwork.backend.common.exception.BadRequestException(
                    ErrorStatus.ALREADY_REGISTER_PHONENUMBER_EXCPETION.getMessage()
            );
        }
        sendSmsCommon(phoneNumber);
    }

    // 아이디 찾기용 (기존 회원 번호로 발송해야 하므로 중복 검증 없음)
    public void sendVerificationSmsForRecovery(String phoneNumber) {
        sendSmsCommon(phoneNumber);
    }

    private void sendSmsCommon(String phoneNumber) {
        initializeMessageService();

        // 기존 인증코드 삭제
        phoneNumberVerificationRepository.findByPhoneNumber(phoneNumber)
                .ifPresent(phoneNumberVerificationRepository::delete);

        // 새 인증코드 생성/저장
        String code = generateSixDigitCode();
        PhoneNumberVerification verification = PhoneNumberVerification.builder()
                .phoneNumber(phoneNumber)
                .code(code)
                .expirationTimeInMinutes(5)
                .isVerified(false)
                .build();
        phoneNumberVerificationRepository.save(verification);

        // SMS 발송
        Message message = new Message();
        message.setFrom(senderPhoneNumber);
        message.setTo(phoneNumber);
        message.setText(String.format("[Korfit 인증코드] %s\n인증코드는 5분 후 만료됩니다.", code));

        try {
            SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
            System.out.println(response);
        } catch (Exception e) {
            throw new InternalServerException(ErrorStatus.SMS_SEND_FAILED_EXCEPTION.getMessage());
        }
    }

    private String generateSixDigitCode() {
        SecureRandom random = new SecureRandom();
        int number = random.nextInt(1000000);
        return String.format("%06d", number);
    }

    // 코드 검증
    public void verifyCode(String code, LocalDateTime requestedAt) {
        PhoneNumberVerification verification = phoneNumberVerificationRepository.findByCode(code)
                .orElseThrow(() -> new com.forwork.backend.common.exception.BadRequestException(
                        ErrorStatus.WRONG_SMS_VERIFICATION_CODE_EXCEPTION.getMessage()
                ));

        if (verification.isExpired(requestedAt)) {
            throw new com.forwork.backend.common.exception.UnauthorizedException(
                    ErrorStatus.UNAUTHORIZED_SMS_VERIFICATION_CODE_EXCEPTION.getMessage()
            );
        }

        verification.setIsVerified(true);
        phoneNumberVerificationRepository.save(verification);
    }

    // 아이디 코드 검증
    public String verifyCodeAndGetPhone(String code, LocalDateTime requestedAt) {

        PhoneNumberVerification verification = phoneNumberVerificationRepository.findByCode(code)
                .orElseThrow(() -> new com.forwork.backend.common.exception.BadRequestException(
                        ErrorStatus.WRONG_SMS_VERIFICATION_CODE_EXCEPTION.getMessage()
                ));

        if (verification.isExpired(requestedAt)) {
            throw new com.forwork.backend.common.exception.UnauthorizedException(
                    ErrorStatus.UNAUTHORIZED_SMS_VERIFICATION_CODE_EXCEPTION.getMessage()
            );
        }

        verification.setIsVerified(true);
        phoneNumberVerificationRepository.save(verification);

        phoneNumberVerificationRepository.delete(verification);

        return verification.getPhoneNumber();
    }
}
