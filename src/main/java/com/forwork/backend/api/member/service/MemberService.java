package com.forwork.backend.api.member.service;

import com.forwork.backend.api.member.dto.MemberModifyIdRequestDTO;
import com.forwork.backend.api.member.dto.MemberRegisterRequestDTO;
import com.forwork.backend.api.member.dto.MemberLoginRequestDTO;
import com.forwork.backend.api.member.dto.MemberLoginResponseDTO;
import com.forwork.backend.api.member.entity.*;
import com.forwork.backend.api.member.jwt.service.JwtService;
import com.forwork.backend.api.member.repository.*;
import com.forwork.backend.common.exception.BadRequestException;
import com.forwork.backend.common.exception.NotFoundException;
import com.forwork.backend.common.response.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PhoneNumberVerificationRepository phoneNumberVerificationRepository;
    private final CompanyValidationRepository companyValidationRepository;
    private final PasswordResetRepository passwordResetRepository;

    // 회원가입
    @Transactional
    public void registerMember(MemberRegisterRequestDTO memberRegisterRequestDTO) {

        // 사용자ID 중복 검증
        if (memberRepository.findByUserId(memberRegisterRequestDTO.getUserId()).isPresent()) {
            throw new BadRequestException(ErrorStatus.ALREADY_REGISTER_USERID_EXCPETION.getMessage());
        }
        // 이메일 중복 검증
        if (memberRepository.findByEmail(memberRegisterRequestDTO.getEmail()).isPresent()) {
            throw new BadRequestException(ErrorStatus.ALREADY_REGISTER_EMAIL_EXCPETION.getMessage());
        }
        // 핸드폰번호 중복 검증
        if (memberRepository.findByPhoneNumber(memberRegisterRequestDTO.getPhoneNumber()).isPresent()) {
            throw new BadRequestException(ErrorStatus.ALREADY_REGISTER_PHONENUMBER_EXCPETION.getMessage());
        }

        // 이메일 인증 여부 체크
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(memberRegisterRequestDTO.getEmail())
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MISSING_EMAIL_VERIFICATION_EXCEPTION.getMessage()));
        if (!emailVerification.isVerified()) {
            throw new BadRequestException(ErrorStatus.MISSING_EMAIL_VERIFICATION_EXCEPTION.getMessage());
        }

        // 핸드폰번호 인증 여부 체크
        PhoneNumberVerification phoneNumberVerification = phoneNumberVerificationRepository.findByPhoneNumber(memberRegisterRequestDTO.getPhoneNumber())
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MISSING_PHONENUMBER_VERIFICATION_EXCEPTION.getMessage()));
        if (!phoneNumberVerification.isVerified()) {
            throw new BadRequestException(ErrorStatus.MISSING_PHONENUMBER_VERIFICATION_EXCEPTION.getMessage());
        }

        Address address = new Address(
                memberRegisterRequestDTO.getZipcode(),
                memberRegisterRequestDTO.getAddress1(),
                memberRegisterRequestDTO.getAddress2()
        );

        // Employee 엔티티 생성
        Member member = Member.builder()
                .userId(memberRegisterRequestDTO.getUserId())
                .password(passwordEncoder.encode(memberRegisterRequestDTO.getPassword()))
                .name(memberRegisterRequestDTO.getName())
                .email(memberRegisterRequestDTO.getEmail())
                .phoneNumber(memberRegisterRequestDTO.getPhoneNumber())
                .address(address)
                .birthday(memberRegisterRequestDTO.getBirthDate())
                .gender(memberRegisterRequestDTO.getGender())
                .nationality(memberRegisterRequestDTO.getNationality())
                .education(memberRegisterRequestDTO.getEducation())
                .visa(memberRegisterRequestDTO.getVisa())
                .termsOfServiceAgreement(memberRegisterRequestDTO.isTermsOfServiceAgreement())
                .isOver15(memberRegisterRequestDTO.isOver15())
                .personalInfoAgreement(memberRegisterRequestDTO.isPersonalInfoAgreement())
                .adInfoAgreementSmsMms(memberRegisterRequestDTO.isAdInfoAgreementSmsMms())
                .adInfoAgreementEmail(memberRegisterRequestDTO.isAdInfoAgreementEmail())
                .role(Role.USER)
                .profileImage(null)
                .build();

        memberRepository.save(member);
    }

    // 로그인
    public MemberLoginResponseDTO login(MemberLoginRequestDTO dto) {
        // userId로 회원 검색
        Member member = memberRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundException(ErrorStatus.USERID_NOT_FOUND_EXCEPTION.getMessage()));

        // 비밀번호 검증
        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new BadRequestException(ErrorStatus.WRONG_PASSWORD_EXCEPTION.getMessage());
        }

        // JWT 토큰 생성 (Access, Refresh)
        Map<String, String> tokens = jwtService.createAccessAndRefreshToken(member.getId());

        // DTO를 사용하여 응답 데이터 구성
        return new MemberLoginResponseDTO(
                member.getName(),
                member.getEmail(),
                member.getUserId(),
                tokens.get("accessToken"),
                tokens.get("refreshToken"),
                member.getRole().name()
        );
    }

    // 사용자 ID 중복 체크
    public void verificationUserId(String userId) {
        // 사용자 ID 중복 검증
        if (memberRepository.findByUserId(userId).isPresent()) {
            throw new BadRequestException(ErrorStatus.ALREADY_REGISTER_USERID_EXCPETION.getMessage());
        }
    }

    // 사용자 ID 검증
    public void verifyMyUserId(String userId, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));

        if (!member.getUserId().equals(userId)) {
            throw new BadRequestException(ErrorStatus.NOT_MATCH_USERID_EXCEPTION.getMessage());
        }
    }

    // 사용자 ID 변경
    @Transactional
    public void modifyUserId(MemberModifyIdRequestDTO memberModifyIdRequestDTO, Long memberId) {

        String newUserId = memberModifyIdRequestDTO.getUserId();

        // 이미 사용 중인 ID인지 확인
        if (memberRepository.findByUserId(newUserId).isPresent()) {
            throw new BadRequestException(ErrorStatus.ALREADY_REGISTER_USERID_EXCPETION.getMessage());
        }

        // 실제 회원이 존재하는지 체크
        memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));
        
        memberRepository.updateUserId(memberId, newUserId);
    }

    // 사용자 비밀번호 검증
    public void verifyMyPassword(String rawPassword, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));

        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new BadRequestException(ErrorStatus.WRONG_PASSWORD_EXCEPTION.getMessage());
        }
    }

    // 사용자 비밀번호 변경
    @Transactional
    public void modifyPassword(String newRawPassword, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));

        String encoded = passwordEncoder.encode(newRawPassword);
        member.updatePassword(encoded);
    }

}