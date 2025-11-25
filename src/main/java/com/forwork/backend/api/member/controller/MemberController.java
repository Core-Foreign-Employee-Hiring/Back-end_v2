package com.forwork.backend.api.member.controller;

import com.forwork.backend.api.member.dto.*;
import com.forwork.backend.api.member.jwt.service.JwtService;
import com.forwork.backend.api.member.service.CompanyValidationService;
import com.forwork.backend.api.member.service.EmailService;
import com.forwork.backend.api.member.service.MemberService;
import com.forwork.backend.api.member.service.MemberSpecificationService;
import com.forwork.backend.common.config.security.SecurityMember;
import com.forwork.backend.common.exception.BadRequestException;
import com.forwork.backend.common.response.ApiResponse;
import com.forwork.backend.common.response.ErrorStatus;
import com.forwork.backend.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "Member 관련 API 입니다.")
@RestController
@RequestMapping("/api/v2/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtService jwtService;
    private final CompanyValidationService companyValidationService;
    private final EmailService emailService;
    private final MemberSpecificationService memberSpecificationService;

    @Operation(
            summary = "회원가입 API (태근)",
            description = "회원가입을 진행합니다. <br>"
                    + "<p>"
                    + "호출 필드 정보) <br>"
                    + "userId : 사용자 ID <br>"
                    + "email : 사용자 이메일 <br>"
                    + "password : 사용자 비밀번호 <br>"
                    + "name : 사용자 이름 <br>"
                    + "phoneNumber : 전화번호 (예시: 01012345678) <br>"
                    + "birthDate : 생년월일 (예시: 2025-01-20) <br>"
                    + "zipcode : 우편번호 (예시: 28111) <br>"
                    + "address1 : 주소 1 (예시: 충북 청주시 서원구 충대로 1) <br>"
                    + "address2 : 주소 2 (예시: 충북대학교 E8-7) <br>"
                    + "nationality : 국적 <br>"
                    + "education : 학력 <br>"
                    + "visa : 비자 <br>"
                    + "gender : 성별 (남자 : MALE / 여자 : FEMALE) <br>"
                    + "<p>"
                    + "termsOfServiceAgreement : 서비스 이용 약관 동의 <br>"
                    + "over15 : 만 15세 이상 확인 <br>"
                    + "personalInfoAgreement : 개인정보 수집 및 이용 동의 <br>"
                    + "adInfoAgreementSmsMms : 광고성 정보 수신 동의 (SMS/MMS) <br>"
                    + "adInfoAgreementEmail : 광고성 정보 수신 동의 (이메일) <br>"
                    + "<p>"
                    + "ENUM : <A href = \"https://www.notion.so/enum-V2-208244b92af280a6b1aac180b3d49df0\" target=\"_blank\"> 이동 하기 </A>"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> registerMember(@RequestBody MemberRegisterRequestDTO memberRegisterRequestDTO) {

        memberService.registerMember(memberRegisterRequestDTO);
        return ApiResponse.success_only(SuccessStatus.SEND_REGISTER_SUCCESS);
    }

    @Operation(
            summary = "로그인 API (태근)",
            description = "ID와 Password를 통해 사용자를 인증하고 토큰을 발급합니다. <br>"
                    + "<p>"
                    + "호출 필드 정보) <br>"
                    + "userId : 사용자 아이디 (예시: user) <br>"
                    + "password : 사용자 비밀번호 (예시: password)"
                    + "<p>"
                    + "요청 예시 : <A href = \"https://www.notion.so/1bc244b92af281f9a82dce6cafca896f?v=1bc244b92af281559243000c1a4fef2f&p=1bc244b92af28125b330c47629fd5152&pm=s\" target=\"_blank\"> 이동 하기 </A>"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "로그인 정보가 유효하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패.")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<MemberLoginResponseDTO>> login(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO) {
        MemberLoginResponseDTO responseDTO = memberService.login(memberLoginRequestDTO);
        return ApiResponse.success(SuccessStatus.SEND_LOGIN_SUCCESS, responseDTO);
    }

    @Operation(
            summary = "토큰 재발급 API (태근)",
            description = "유효한 리프레시 토큰을 헤더(Authorization-Refresh)로 제공하면 새로운 액세스 토큰과 리프레시 토큰을 발급하여 헤더로 전송합니다. / [주의] 스웨거로 테스트할때 토큰 앞에 'Bearer ' 을 붙여야 함"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "리프레시 토큰이 입력되지 않았습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "유효하지 않은 리프레시 토큰입니다."),
    })
    @GetMapping("/token-reissue")
    public ResponseEntity<ApiResponse<Void>> reissueToken(@RequestHeader(value = "Authorization-Refresh", required = false) String refreshToken) {

        // 리프레시 토큰이 입력되지 않았을 경우 예외 처리
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new BadRequestException(ErrorStatus.MISSING_REFRESH_TOKEN_EXCEPTION.getMessage());
        }

        // "Bearer " 문자열 제거 후 토큰 검증
        String pureRefreshToken = refreshToken.substring(7);
        if (!jwtService.isTokenValid(pureRefreshToken)) {
            // 유효하지 않은 토큰 예외 처리
            throw new BadRequestException(ErrorStatus.UNAUTHORIZED_REFRESH_TOKEN_EXCEPTION.getMessage());
        }

        return ApiResponse.success_only(SuccessStatus.SEND_REISSUE_TOKEN_SUCCESS);
    }

    @Operation(
            summary = "사업자등록 정보 진위 확인. API (용범)",
            description = "사업자등록 정보의 진위 여부를 확인합니다." +
                    "<p>" +
                    "호출 필드 정보) <br>" +
                    "businessNo: 사업자등록번호('-' 없음)<br>" +
                    "startDate: 개업일자 (YYYYMMDD 포맷)<br>" +
                    "representativeName: 대표자 성명<br>"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사업자등록 정보 진위 조회 완료."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @PostMapping(value = "/employer/company-validate")
    public ResponseEntity<ApiResponse<Boolean>> isCompanyValidate(@RequestBody BusinessVerificationRequestDTO businessVerificationRequestDTO) {

        boolean companyValidate = companyValidationService.isCompanyValidate(businessVerificationRequestDTO.getBusinessNo(), businessVerificationRequestDTO.getStartDate(), businessVerificationRequestDTO.getRepresentativeName());
        return ApiResponse.success(SuccessStatus.SEND_COMPANY_VALIDATION_COMPLETED, companyValidate);
    }

    @Operation(
            summary = "사용자 ID 중복 체크 API (태근)",
            description = "해당 사용자 ID가 사용가능한지 체크합니다. <br>"
                    + "<p>"
                    + "호출 필드 정보) <br>"
                    + "userId : 사용자 ID"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사용자 ID 사용 가능"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미 등록된 사용자 ID 입니다."),
    })
    @GetMapping("/verify-userid")
    public ResponseEntity<ApiResponse<Void>> getUserIdVerification(@RequestParam("userId") String userId) {

        memberService.verificationUserId(userId);
        return ApiResponse.success_only(SuccessStatus.SEND_ALLOW_USERID_SUCCESS);
    }

    @Operation(
            summary = "현재 사용자 아이디 체크 API (태근)",
            description = "해당 사용자 ID가 일치하는지 체크합니다. <br>"
                    + "<p>"
                    + "호출 필드 정보) <br>"
                    + "userId : 사용자 ID"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사용자 ID 확인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "일치하지 않는 사용자 ID 입니다."),
    })
    @GetMapping("/verify-my-userid")
    public ResponseEntity<ApiResponse<Void>> verifyMyUserId(@RequestParam("userId") String userId, @AuthenticationPrincipal SecurityMember securityMember) {

        memberService.verifyMyUserId(userId, securityMember.getId());
        return ApiResponse.success_only(SuccessStatus.SEND_VERIFY_MY_USERID_SUCCESS);
    }

    @Operation(
            summary = "아이디 변경 API (태근)",
            description = "해당 사용자 ID를 변경합니다. <br>"
                    + "<p>"
                    + "호출 필드 정보) <br>"
                    + "userId : 사용자 ID"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사용자 ID 변경 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미 사용중인 사용장 ID 입니다."),
    })
    @PatchMapping("/modify-userid")
    public ResponseEntity<ApiResponse<Void>> modifyUserId(@RequestBody MemberModifyIdRequestDTO memberModifyIdRequestDTO, @AuthenticationPrincipal SecurityMember securityMember) {

        memberService.modifyUserId(memberModifyIdRequestDTO, securityMember.getId());
        return ApiResponse.success_only(SuccessStatus.SEND_MODIFY_USERID_SUCCESS);
    }

    @Operation(
            summary = "기존 비밀번호 확인 API (태근)",
            description = "현재 로그인된 사용자의 비밀번호가 일치하는지 확인합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "현재 비밀번호 검증 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않습니다.")
    })
    @PostMapping("/verify-my-password")
    public ResponseEntity<ApiResponse<Void>> verifyMyPassword(
            @RequestBody MemberModifyPasswordRequestDTO dto,
            @AuthenticationPrincipal SecurityMember securityMember
    ) {
        memberService.verifyMyPassword(dto.getPassword(), securityMember.getId());
        return ApiResponse.success_only(SuccessStatus.SEND_VERIFY_MY_PASSWORD_SUCCESS);
    }

    @Operation(
            summary = "비밀번호 변경 API (태근)",
            description = "현재 로그인된 사용자의 비밀번호를 새로운 비밀번호로 변경합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @PatchMapping("/modify-password")
    public ResponseEntity<ApiResponse<Void>> modifyPassword(
            @RequestBody MemberModifyPasswordRequestDTO dto,
            @AuthenticationPrincipal SecurityMember securityMember
    ) {
        memberService.modifyPassword(dto.getPassword(), securityMember.getId());
        return ApiResponse.success_only(SuccessStatus.SEND_MODIFY_PASSWORD_SUCCESS);
    }

    @Operation(
            summary = "현재 사용자 마이페이지 정보 조회 API (태근)",
            description = "현재 로그인된 사용자의 회원정보(개인정보 + 동의 4종)를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "마이페이지 정보 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.")
    })
    @GetMapping("/my-profile")
    public ResponseEntity<ApiResponse<MemberProfileResponseDTO>> getMyProfile(
            @AuthenticationPrincipal SecurityMember securityMember
    ) {
        MemberProfileResponseDTO memberProfileResponseDTO = memberService.getMyProfile(securityMember.getId());
        return ApiResponse.success(SuccessStatus.SEND_PROFILE_INFO_SUCCESS, memberProfileResponseDTO);
    }

    @Operation(
            summary = "회원정보 수정 API (태근)",
            description = """
                    변경 가능한 필드: 이름, 이메일(인증 필요), 연락처(인증 필요),
                    주소(zipcode/address1/address2), 생년월일, 국적, 비자, 학력, 성별,
                    그리고 동의 항목 4가지(termsOfServiceAgreement(서비스 이용약관), personalInfoAgreement(개인정보 수집 및 이용), adInfoAgreementSmsMms(광고성 - SNS/MMS), adInfoAgreementEmail(광고성 - 이메일).<br>
                    - 각 필드는 값이 기존과 동일하면 무시하고, 다를 때만 갱신합니다.<br>
                    - 이메일/연락처는 변경 시 각각의 인증이 완료되야 합니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "프로필 변경 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 이메일/휴대폰입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.")
    })
    @PatchMapping("/modify-profile")
    public ResponseEntity<ApiResponse<Void>> modifyProfile(@RequestBody MemberUpdateRequestDTO memberUpdateRequestDTO, @AuthenticationPrincipal SecurityMember securityMember) {

        memberService.modifyProfile(securityMember.getId(), memberUpdateRequestDTO);
        return ApiResponse.success_only(SuccessStatus.SEND_PROFILE_UPDATE_SUCCESS);
    }

    @Operation(
            summary = "아이디 찾기 - 인증코드 발송 API (태근)",
            description = "이름과 휴대폰 번호가 일치하는 회원을 찾은 뒤, 해당 번호로 인증코드를 발송합니다.<br>"
                    + "<p>"
                    + "호출 필드 정보) <br>"
                    + "name : 사용자 이름 <br>"
                    + "phoneNumber : 전화번호 (예시 : 01012345678)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "아이디 찾기 인증코드 발송 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 이름/전화번호의 회원을 찾을 수 없습니다.")
    })
    @PostMapping("/find-id/send-code")
    public ResponseEntity<ApiResponse<Void>> sendFindIdCode(@RequestBody FindIdRequestDTO findIdRequestDTO) {

        memberService.sendFindIdVerificationCode(findIdRequestDTO);
        return ApiResponse.success_only(SuccessStatus.SEND_SMS_VERIFICATION_CODE_SUCCESS);
    }

    @Operation(
            summary = "아이디 찾기 - 코드 검증 & 결과 조회 API (태근)",
            description = "휴대폰으로 발송된 인증코드를 검증하고, 일치하면 해당 회원의 ID와 생성일자를 반환합니다.<br>"
                    + "<p>"
                    + "호출 필드 정보) <br>"
                    + "code : 문자로 발송된 인증코드"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "아이디 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증코드가 만료되었거나 올바르지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없습니다.")
    })
    @PostMapping("/find-id/verify-code")
    public ResponseEntity<ApiResponse<FindIdResponseDTO>> verifyFindIdCode(@RequestBody SmsVerificationCodeRequestDTO smsVerificationCodeReqeustDTO) {

        FindIdResponseDTO findIdResponseDTO = memberService.verifyFindIdCode(smsVerificationCodeReqeustDTO.getCode());
        return ApiResponse.success(SuccessStatus.SEND_VERIFY_SMS_CODE_SUCCESS, findIdResponseDTO);
    }

    @Operation(
            summary = "비밀번호 재설정 - 코드 발송 API (태근)",
            description = "아이디, 이름, 이메일이 모두 일치하는 계정을 찾은 후 해당 이메일로 비밀번호 재설정 인증코드를 발송합니다.<br>"
                    + "<p>"
                    + "호출 필드 정보) <br>"
                    + "userId : 사용자 아이디 <br>"
                    + "name : 사용자 이름 <br>"
                    + "email : 사용자 이메일"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "비밀번호 재설정 인증코드 발송 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "계정을 찾을 수 없습니다.")
    })
    @PostMapping("/password-reset/send-code")
    public ResponseEntity<ApiResponse<Void>> sendPasswordResetCode(@RequestBody PasswordResetRequestDTO passwordResetRequestDTO) {

        memberService.sendPasswordResetCode(passwordResetRequestDTO);
        return ApiResponse.success_only(SuccessStatus.SEND_EMAIL_VERIFICATION_CODE_SUCCESS);
    }

    @Operation(
            summary = "비밀번호 재설정 - 코드 검증 API (태근)",
            description = "이메일로 발송된 인증코드를 검증합니다. 코드가 유효하면 비밀번호 변경을 진행할 수 있습니다.<br>"
                    + "<p>"
                    + "호출 필드 정보) <br>"
                    + "code : 이메일 인증코드"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "인증코드 검증 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증코드가 만료되었습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "인증코드가 올바르지 않습니다.")
    })
    @PostMapping("/password-reset/verify-code")
    public ResponseEntity<ApiResponse<Void>> verifyPasswordResetCode(@RequestBody EmailVerificationCodeRequestDTO emailVerificationCodeRequestDTO) {

        memberService.verifyPasswordResetCode(emailVerificationCodeRequestDTO.getCode());
        return ApiResponse.success_only(SuccessStatus.SEND_EMAIL_VERIFICATION_SUCCESS);
    }

    @Operation(
            summary = "비밀번호 재설정 - 변경 API (태근)",
            description = "유효한 인증코드와 새 비밀번호를 전달하면 비밀번호를 변경합니다.<br>"
                    + "<p>"
                    + "호출 필드 정보) <br>"
                    + "code : 이메일 인증코드 <br>"
                    + "newPassword : 새 비밀번호"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증코드가 만료되었습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "인증코드가 올바르지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없습니다.")
    })
    @PatchMapping("/password-reset/modify")
    public ResponseEntity<ApiResponse<Void>> changePasswordByReset(@RequestBody PasswordResetConfirmDTO passwordResetConfirmDTO) {

        memberService.changePasswordByReset(passwordResetConfirmDTO);
        return ApiResponse.success_only(SuccessStatus.SEND_MODIFY_PASSWORD_SUCCESS);
    }

    @Operation(
            summary = "내 스펙 입력 API (용범)", description = "입력= MemberSpecificationRequestDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스펙 등록 성공"),
    })
    @PostMapping("/specification")
    public ResponseEntity<ApiResponse<Void>> createMemberSpecification(@AuthenticationPrincipal SecurityMember securityMember,
                                                                       @Valid @RequestBody MemberSpecificationRequestDTO memberSpecificationRequestDTO) {

        memberSpecificationService.createMemberSpecification(securityMember.getId(), memberSpecificationRequestDTO);
        return ApiResponse.success_only(SuccessStatus.CREATE_SPEC_SUCCESS);
    }

    @Operation(
            summary = "내 스펙 조회 API (용범)",
            description = "출력= MemberSpecificationRequestDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스펙 조회 성공"),
    })
    @GetMapping("/specification")
    public ResponseEntity<ApiResponse<MemberSpecificationResponseDTO>> getMemberSpecification(@AuthenticationPrincipal SecurityMember securityMember) {

        MemberSpecificationResponseDTO response = memberSpecificationService.getMemberSpecification(securityMember.getId());

        return ApiResponse.success(SuccessStatus.GET_MEMBER_SPECIFICATION_SUCCESS, response);
    }

}