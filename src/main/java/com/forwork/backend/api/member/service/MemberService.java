package com.forwork.backend.api.member.service;

import com.forwork.backend.api.member.dto.*;
import com.forwork.backend.api.member.entity.*;
import com.forwork.backend.api.member.jwt.service.JwtService;
import com.forwork.backend.api.member.repository.*;
import com.forwork.backend.common.exception.BadRequestException;
import com.forwork.backend.common.exception.NotFoundException;
import com.forwork.backend.common.exception.UnauthorizedException;
import com.forwork.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final SmsService smsService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PhoneNumberVerificationRepository phoneNumberVerificationRepository;
    private final CompanyValidationRepository companyValidationRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final JobRoleEntityRepository jobRoleEntityRepository;
    private final MemberJobRoleRepository memberJobRoleRepository;

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

        /*// 이메일 인증 여부 체크
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
        }*/

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
                .nationality(memberRegisterRequestDTO.getNationality().getDbValue())
                .education(memberRegisterRequestDTO.getEducation())
                .visa(memberRegisterRequestDTO.getVisa().getDbValue())
                .termsOfServiceAgreement(memberRegisterRequestDTO.isTermsOfServiceAgreement())
                .isOver15(memberRegisterRequestDTO.isOver15())
                .personalInfoAgreement(memberRegisterRequestDTO.isPersonalInfoAgreement())
                .adInfoAgreementSmsMms(memberRegisterRequestDTO.isAdInfoAgreementSmsMms())
                .adInfoAgreementEmail(memberRegisterRequestDTO.isAdInfoAgreementEmail())
                .role(Role.USER)
                .profileImage(null)
                .build();

        memberRepository.save(member).getId();

        /*
        * 직무 처리
        * */

        Set<JobRole> jobRoles = memberRegisterRequestDTO.getJobRoles();
        List<JobRoleEntity> allByJobRoles =
                jobRoleEntityRepository.findAllByJobRoles(jobRoles.stream().map(JobRole::getDbValue).toList());

        Set<MemberJobRole> recruitJobRoles=new HashSet<>();

        for (JobRoleEntity jobRoleEntity : allByJobRoles) {
            MemberJobRole memberJobRole = new MemberJobRole(member, jobRoleEntity);
            recruitJobRoles.add(memberJobRole);
        }

        memberJobRoleRepository.saveAll(recruitJobRoles);
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

    // 내 마이페이지 정보 조회
    @Transactional(readOnly = true)
    public MemberProfileResponseDTO getMyProfile(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));

        // 직무
        Set<MemberJobRole> jobRoles = new HashSet<>(memberJobRoleRepository.findByMemberId(memberId));
        List<JobRole> jobRoles1 = JobRole.convertToJobRolesByMember(jobRoles);

        return new MemberProfileResponseDTO(
                member.getName(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getAddress() != null ? member.getAddress().getZipcode() : null,
                member.getAddress() != null ? member.getAddress().getAddress1() : null,
                member.getAddress() != null ? member.getAddress().getAddress2() : null,
                member.getBirthday(),
                member.getGender(),
                Nationality.getNationalityByDBValue(member.getNationality()) ,
                Visa.getVisaByDBValue(member.getVisa()) ,
                member.getEducation(),
                jobRoles1,
                member.isTermsOfServiceAgreement(),
                member.isPersonalInfoAgreement(),
                member.isAdInfoAgreementSmsMms(),
                member.isAdInfoAgreementEmail()
        );
    }

    // 내 프로필 수정
    @Transactional
    public void modifyProfile(Long memberId, MemberUpdateRequestDTO memberUpdateRequestDTO) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));

        // 이름
        if (memberUpdateRequestDTO.getName() != null && !memberUpdateRequestDTO.getName().equals(member.getName())) {
            member.updateName(memberUpdateRequestDTO.getName());
        }

        // 이메일 (다를 때만), 중복 + 인증 체크 -> 성공 시 verification delete
        if (memberUpdateRequestDTO.getEmail() != null && !memberUpdateRequestDTO.getEmail().equals(member.getEmail())) {

            if (memberRepository.findByEmail(memberUpdateRequestDTO.getEmail()).isPresent()) {
                throw new BadRequestException(ErrorStatus.ALREADY_REGISTER_EMAIL_EXCPETION.getMessage());
            }

            EmailVerification ev = emailVerificationRepository.findByEmail(memberUpdateRequestDTO.getEmail())
                    .orElseThrow(() -> new BadRequestException(ErrorStatus.MISSING_EMAIL_VERIFICATION_EXCEPTION.getMessage()));

            if (!ev.isVerified()) {
                throw new BadRequestException(ErrorStatus.MISSING_EMAIL_VERIFICATION_EXCEPTION.getMessage());
            }
            if (ev.isExpired(LocalDateTime.now())) {
                throw new com.forwork.backend.common.exception.UnauthorizedException(
                        ErrorStatus.UNAUTHORIZED_EMAIL_VERIFICATION_CODE_EXCEPTION.getMessage()
                );
            }

            member.updateEmail(memberUpdateRequestDTO.getEmail());

            emailVerificationRepository.delete(ev);
        }

        // 휴대폰번호 (다를 때만), 중복 + 인증 체크 -> 성공 시 verification delete
        if (memberUpdateRequestDTO.getPhoneNumber() != null && !memberUpdateRequestDTO.getPhoneNumber().equals(member.getPhoneNumber())) {

            if (memberRepository.findByPhoneNumber(memberUpdateRequestDTO.getPhoneNumber()).isPresent()) {
                throw new BadRequestException(ErrorStatus.ALREADY_REGISTER_PHONENUMBER_EXCPETION.getMessage());
            }

            PhoneNumberVerification pv = phoneNumberVerificationRepository.findByPhoneNumber(memberUpdateRequestDTO.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException(ErrorStatus.MISSING_PHONENUMBER_VERIFICATION_EXCEPTION.getMessage()));

            if (!pv.isVerified()) {
                throw new BadRequestException(ErrorStatus.MISSING_PHONENUMBER_VERIFICATION_EXCEPTION.getMessage());
            }
            if (pv.isExpired(LocalDateTime.now())) {
                throw new com.forwork.backend.common.exception.UnauthorizedException(
                        ErrorStatus.UNAUTHORIZED_SMS_VERIFICATION_CODE_EXCEPTION.getMessage()
                );
            }

            member.updatePhoneNumber(memberUpdateRequestDTO.getPhoneNumber());

            phoneNumberVerificationRepository.delete(pv);
        }

        // 주소 (세 필드 중 하나라도 달라지면 갱신)
        boolean hasAddressChange =
                (memberUpdateRequestDTO.getZipcode() != null && (member.getAddress() == null || !memberUpdateRequestDTO.getZipcode().equals(member.getAddress().getZipcode()))) ||
                        (memberUpdateRequestDTO.getAddress1() != null && (member.getAddress() == null || !memberUpdateRequestDTO.getAddress1().equals(member.getAddress().getAddress1()))) ||
                        (memberUpdateRequestDTO.getAddress2() != null && (member.getAddress() == null || !memberUpdateRequestDTO.getAddress2().equals(member.getAddress().getAddress2())));

        if (hasAddressChange) {
            Address newAddress = new Address(
                    memberUpdateRequestDTO.getZipcode() != null ? memberUpdateRequestDTO.getZipcode() : (member.getAddress() != null ? member.getAddress().getZipcode() : null),
                    memberUpdateRequestDTO.getAddress1() != null ? memberUpdateRequestDTO.getAddress1() : (member.getAddress() != null ? member.getAddress().getAddress1() : null),
                    memberUpdateRequestDTO.getAddress2() != null ? memberUpdateRequestDTO.getAddress2() : (member.getAddress() != null ? member.getAddress().getAddress2() : null)
            );
            member.updateAddress(newAddress);
        }

        // 생년월일
        if (memberUpdateRequestDTO.getBirthDate() != null && !memberUpdateRequestDTO.getBirthDate().equals(member.getBirthday())) {
            member.updateBirthday(memberUpdateRequestDTO.getBirthDate());
        }

        // 국적/비자/학력/성별
        if (memberUpdateRequestDTO.getNationality() != null && !memberUpdateRequestDTO.getNationality().equals(member.getNationality())) {
            member.updateNationality(memberUpdateRequestDTO.getNationality().getDbValue());
        }
        if (memberUpdateRequestDTO.getVisa() != null && !memberUpdateRequestDTO.getVisa().equals(member.getVisa())) {
            member.updateVisa(memberUpdateRequestDTO.getVisa().getDbValue());
        }
        if (memberUpdateRequestDTO.getEducation() != null && !memberUpdateRequestDTO.getEducation().equals(member.getEducation())) {
            member.updateEducation(memberUpdateRequestDTO.getEducation());
        }
        if (memberUpdateRequestDTO.getGender() != null && memberUpdateRequestDTO.getGender() != member.getGender()) {
            member.updateGender(memberUpdateRequestDTO.getGender());
        }

        // 동의 항목 4가지: null이면 미변경, 값이 있고 기존과 다르면 갱신
        if (memberUpdateRequestDTO.getTermsOfServiceAgreement() != null
                && memberUpdateRequestDTO.getTermsOfServiceAgreement() != member.isTermsOfServiceAgreement()) {
            member.updateTermsOfServiceAgreement(memberUpdateRequestDTO.getTermsOfServiceAgreement());
        }
        if (memberUpdateRequestDTO.getPersonalInfoAgreement() != null
                && memberUpdateRequestDTO.getPersonalInfoAgreement() != member.isPersonalInfoAgreement()) {
            member.updatePersonalInfoAgreement(memberUpdateRequestDTO.getPersonalInfoAgreement());
        }
        if (memberUpdateRequestDTO.getAdInfoAgreementSmsMms() != null
                && memberUpdateRequestDTO.getAdInfoAgreementSmsMms() != member.isAdInfoAgreementSmsMms()) {
            member.updateAdInfoAgreementSmsMms(memberUpdateRequestDTO.getAdInfoAgreementSmsMms());
        }
        if (memberUpdateRequestDTO.getAdInfoAgreementEmail() != null
                && memberUpdateRequestDTO.getAdInfoAgreementEmail() != member.isAdInfoAgreementEmail()) {
            member.updateAdInfoAgreementEmail(memberUpdateRequestDTO.getAdInfoAgreementEmail());
        }

        /*
         * 직무
         * */

        Set<MemberJobRole> memberJobRoles = new HashSet<>(memberJobRoleRepository.findByMemberId(memberId));

        List<JobRole> oldJobRoles = JobRole.convertToJobRolesByMember(memberJobRoles);
        List<JobRole> newJobRoles = memberUpdateRequestDTO.getJobRoles().stream().toList();

        // add= new-old
        List<JobRole> doAddJobRoles=new ArrayList<>(newJobRoles);

        doAddJobRoles.removeAll(oldJobRoles);
        List<JobRoleEntity> allByJobRoles = jobRoleEntityRepository.findAllByJobRoles(doAddJobRoles.stream().map(JobRole::getDbValue).toList());

        List<MemberJobRole> toAddEntityJobRole=new ArrayList<>();
        for (JobRoleEntity allByJobRole : allByJobRoles) {
            MemberJobRole memberJobRole = new MemberJobRole(member, allByJobRole);
            toAddEntityJobRole.add(memberJobRole);

        }

        // delete= old-new
        List<MemberJobRole>toDeleteJobRole=new ArrayList<>();
        for(MemberJobRole memberJobRole : memberJobRoles){
            if(!newJobRoles.contains(JobRole.getJobRole(memberJobRole.getJobRoleEntity().getJobRole()))){
                toDeleteJobRole.add(memberJobRole);
            }
        }

        memberJobRoleRepository.saveAll(toAddEntityJobRole);
        if(!toDeleteJobRole.isEmpty()){memberJobRoleRepository.deleteAll(toDeleteJobRole);}

    }

    // 아이디 찾기 1단계: 이름+전화번호로 회원 존재 확인 후, 해당 번호로 인증코드 발송
    @Transactional
    public void sendFindIdVerificationCode(FindIdRequestDTO findIdRequestDTO) {

        // 회원 찾기
        memberRepository.findByNameAndPhoneNumber(findIdRequestDTO.getName(), findIdRequestDTO.getPhoneNumber())
                .orElseThrow(() -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));

        // 해당 번호로 인증코드 발송
        smsService.sendVerificationSmsForRecovery(findIdRequestDTO.getPhoneNumber());
    }

    // 아이디 찾기 2단계: 인증코드 검증 후 아이디랑 생성일자 반환
    @Transactional(readOnly = true)
    public FindIdResponseDTO verifyFindIdCode(String code) {
        LocalDateTime requestedAt = LocalDateTime.now();

        String phoneNumber = smsService.verifyCodeAndGetPhone(code, requestedAt);

        Member member = memberRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));

        return new FindIdResponseDTO(member.getUserId(), member.getCreatedAt());
    }

    // 비밀번호 재설정: 1) 코드 발송
    public void sendPasswordResetCode(PasswordResetRequestDTO passwordResetRequestDTO) {

        emailService.sendPasswordResetEmail(passwordResetRequestDTO);
    }

    //  비밀번호 재설정: 2) 코드 검증
    @Transactional(readOnly = true)
    public void verifyPasswordResetCode(String code) {
        LocalDateTime now = LocalDateTime.now();

        PasswordReset passwordReset = passwordResetRepository.findByCode(code)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.WRONG_EMAIL_VERIFICATION_CODE_EXCEPTION.getMessage()));

        if (passwordReset.getExpirationTime().isBefore(now)) {
            throw new UnauthorizedException(ErrorStatus.UNAUTHORIZED_EMAIL_VERIFICATION_CODE_EXCEPTION.getMessage());
        }

    }

    // 비밀번호 재설정: 3) 비밀번호 변경
    public void changePasswordByReset(PasswordResetConfirmDTO passwordResetConfirmDTO) {
        LocalDateTime now = LocalDateTime.now();

        // 안전하게 한번 더 코드 유효성 검사
        PasswordReset pr = passwordResetRepository.findByCode(passwordResetConfirmDTO.getCode())
                .orElseThrow(() -> new BadRequestException(ErrorStatus.WRONG_EMAIL_VERIFICATION_CODE_EXCEPTION.getMessage()));

        if (pr.getExpirationTime().isBefore(now)) {
            throw new UnauthorizedException(ErrorStatus.UNAUTHORIZED_EMAIL_VERIFICATION_CODE_EXCEPTION.getMessage());
        }

        Member member = memberRepository.findByEmail(pr.getEmail())
                .orElseThrow(() -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));

        // 비밀번호 변경
        String encoded = passwordEncoder.encode(passwordResetConfirmDTO.getNewPassword());
        member.updatePassword(encoded);

        passwordResetRepository.delete(pr);
    }

}