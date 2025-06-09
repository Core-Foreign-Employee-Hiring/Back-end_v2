package com.forwork.backend.api.member.service;

import com.forwork.backend.api.member.dto.EmployeeRegisterRequestDTO;
import com.forwork.backend.api.member.dto.EmployerRegisterRequestDTO;
import com.forwork.backend.api.member.dto.MemberLoginRequestDTO;
import com.forwork.backend.api.member.dto.MemberLoginResponseDTO;
import com.forwork.backend.api.member.entity.*;
import com.forwork.backend.api.member.jwt.service.JwtService;
import com.forwork.backend.api.member.repository.*;
import com.forwork.backend.common.exception.BadRequestException;
import com.forwork.backend.common.exception.NotFoundException;
import com.forwork.backend.common.exception.UnauthorizedException;
import com.forwork.backend.common.response.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.filter.OrderedFormContentFilter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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

    // 고용인 회원가입
    @Transactional
    public void registerEmployee(EmployeeRegisterRequestDTO employeeRegisterRequestDTO) {

        // 사용자ID 중복 검증
        if (memberRepository.findByUserId(employeeRegisterRequestDTO.getUserId()).isPresent()) {
            throw new BadRequestException(ErrorStatus.ALREADY_REGISTER_USERID_EXCPETION.getMessage());
        }
        // 이메일 중복 검증
        if (memberRepository.findByEmail(employeeRegisterRequestDTO.getEmail()).isPresent()) {
            throw new BadRequestException(ErrorStatus.ALREADY_REGISTER_EMAIL_EXCPETION.getMessage());
        }
        // 핸드폰번호 중복 검증
        if (memberRepository.findByPhoneNumber(employeeRegisterRequestDTO.getPhoneNumber()).isPresent()) {
            throw new BadRequestException(ErrorStatus.ALREADY_REGISTER_PHONENUMBER_EXCPETION.getMessage());
        }

        // 이메일 인증 여부 체크
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(employeeRegisterRequestDTO.getEmail())
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MISSING_EMAIL_VERIFICATION_EXCEPTION.getMessage()));
        if (!emailVerification.isVerified()) {
            throw new BadRequestException(ErrorStatus.MISSING_EMAIL_VERIFICATION_EXCEPTION.getMessage());
        }

        // 핸드폰번호 인증 여부 체크
        PhoneNumberVerification phoneNumberVerification = phoneNumberVerificationRepository.findByPhoneNumber(employeeRegisterRequestDTO.getPhoneNumber())
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MISSING_PHONENUMBER_VERIFICATION_EXCEPTION.getMessage()));
        if (!phoneNumberVerification.isVerified()) {
            throw new BadRequestException(ErrorStatus.MISSING_PHONENUMBER_VERIFICATION_EXCEPTION.getMessage());
        }

        Address address = new Address(
                employeeRegisterRequestDTO.getZipcode(),
                employeeRegisterRequestDTO.getAddress1(),
                employeeRegisterRequestDTO.getAddress2()
        );

        // Employee 엔티티 생성
        Employee employee = new Employee(
                employeeRegisterRequestDTO.getUserId(),
                passwordEncoder.encode(employeeRegisterRequestDTO.getPassword()), // 비밀번호 암호화
                employeeRegisterRequestDTO.getName(),
                employeeRegisterRequestDTO.getEmail(),
                employeeRegisterRequestDTO.getPhoneNumber(),
                address,
                employeeRegisterRequestDTO.getNationality(),
                employeeRegisterRequestDTO.getEducation(),
                employeeRegisterRequestDTO.getVisa(),
                employeeRegisterRequestDTO.getBirthDate(),
                employeeRegisterRequestDTO.getGender(),
                employeeRegisterRequestDTO.isTermsOfServiceAgreement(),
                employeeRegisterRequestDTO.isOver15(),
                employeeRegisterRequestDTO.isPersonalInfoAgreement(),
                employeeRegisterRequestDTO.isAdInfoAgreementSmsMms(),
                employeeRegisterRequestDTO.isAdInfoAgreementEmail()
        );

        memberRepository.save(employee);
    }

    // 고용주 회원가입
    @Transactional
    public void registerEmployer(EmployerRegisterRequestDTO employerRegisterRequestDTO) {

        // 사용자ID 중복 검증
        if (memberRepository.findByUserId(employerRegisterRequestDTO.getUserId()).isPresent()) {
            throw new BadRequestException(ErrorStatus.ALREADY_REGISTER_USERID_EXCPETION.getMessage());
        }
        // 이메일 중복 검증
        if (memberRepository.findByEmail(employerRegisterRequestDTO.getEmail()).isPresent()) {
            throw new BadRequestException(ErrorStatus.ALREADY_REGISTER_EMAIL_EXCPETION.getMessage());
        }
        // 핸드폰번호 중복 검증
        if (memberRepository.findByPhoneNumber(employerRegisterRequestDTO.getPhoneNumber()).isPresent()) {
            throw new BadRequestException(ErrorStatus.ALREADY_REGISTER_PHONENUMBER_EXCPETION.getMessage());
        }

        // 이메일 인증 여부 체크
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(employerRegisterRequestDTO.getEmail())
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MISSING_EMAIL_VERIFICATION_EXCEPTION.getMessage()));
        if (!emailVerification.isVerified()) {
            throw new BadRequestException(ErrorStatus.MISSING_EMAIL_VERIFICATION_EXCEPTION.getMessage());
        }

        // 핸드폰번호 인증 여부 체크
        PhoneNumberVerification phoneNumberVerification = phoneNumberVerificationRepository.findByPhoneNumber(employerRegisterRequestDTO.getPhoneNumber())
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MISSING_PHONENUMBER_VERIFICATION_EXCEPTION.getMessage()));
        if (!phoneNumberVerification.isVerified()) {
            throw new BadRequestException(ErrorStatus.MISSING_PHONENUMBER_VERIFICATION_EXCEPTION.getMessage());
        }

        // 사업자등록번호인증 확인

        String startDate = employerRegisterRequestDTO.getEstablishedDate().toString().replace("-", ""); // "yyyy-MM-dd" -> "yyyyMMdd"

        Optional<CompanyValidation> cv = companyValidationRepository.findByBusinessNoAndStartDateAndRepresentativeName(employerRegisterRequestDTO.getBusinessRegistrationNumber(), startDate, employerRegisterRequestDTO.getName());
        if(cv.isEmpty()){
            log.error("사업자 인증 안 됨.");
            log.info("사업자 등록 번호= {}", employerRegisterRequestDTO.getBusinessRegistrationNumber());
            log.info("startDate= {}", startDate);
            log.info("대표자명= {}", employerRegisterRequestDTO.getName());
            throw new BadRequestException(ErrorStatus.MISSING_BUSINESS_REGISTRATION_VERIFICATION_EXCEPTION.getMessage());
        }

        companyValidationRepository.delete(cv.get());
        Address address = new Address(
                employerRegisterRequestDTO.getZipcode(),
                employerRegisterRequestDTO.getAddress1(),
                employerRegisterRequestDTO.getAddress2()
        );

        // Employer 엔티티 생성
        Employer employer = new Employer(
                employerRegisterRequestDTO.getUserId(),
                passwordEncoder.encode(employerRegisterRequestDTO.getPassword()), // 비밀번호 암호화
                employerRegisterRequestDTO.getName(),
                employerRegisterRequestDTO.getEmail(),
                employerRegisterRequestDTO.getPhoneNumber(),
                address,
                employerRegisterRequestDTO.getBusinessRegistrationNumber(),
                employerRegisterRequestDTO.getCompanyName(),
                employerRegisterRequestDTO.getEstablishedDate(),
                employerRegisterRequestDTO.getBirthDate(),
                employerRegisterRequestDTO.getGender(),
                employerRegisterRequestDTO.getCompanyType(),
                employerRegisterRequestDTO.getJobCategory(),
                employerRegisterRequestDTO.isTermsOfServiceAgreement(),
                employerRegisterRequestDTO.isOver15(),
                employerRegisterRequestDTO.isPersonalInfoAgreement(),
                employerRegisterRequestDTO.isAdInfoAgreementSmsMms(),
                employerRegisterRequestDTO.isAdInfoAgreementEmail()
        );

        memberRepository.save(employer);
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

}