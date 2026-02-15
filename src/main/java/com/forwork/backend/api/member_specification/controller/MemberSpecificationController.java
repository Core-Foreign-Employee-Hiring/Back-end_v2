package com.forwork.backend.api.member_specification.controller;

import com.forwork.backend.api.member_specification.dto.request.*;
import com.forwork.backend.api.member_specification.dto.response.MemberSpecEvaluationResponseDTO;
import com.forwork.backend.api.member_specification.dto.response.MemberSpecificationResponseDTO;
import com.forwork.backend.api.member_specification.service.MemberSpecificationService;
import com.forwork.backend.common.config.security.SecurityMember;
import com.forwork.backend.common.response.ApiResponse;
import com.forwork.backend.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "MemberSpecification", description = "Member 스펙 관련 API 입니다.")
@RestController
@RequestMapping("/api/v2/member/specification")
@RequiredArgsConstructor
public class MemberSpecificationController {
    private final MemberSpecificationService memberSpecificationService;

    /*
     * 스펙
     * */

    /*
     * c
     * */

    @Operation(
            summary = "학력 입력 API (용범)", description = "입력= EducationRequestDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "스펙 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @PostMapping("/education")
    public ResponseEntity<ApiResponse<Void>> createEducation(@AuthenticationPrincipal SecurityMember securityMember,
                                                             @Valid @RequestBody EducationRequestDTO requestDTO) {

        memberSpecificationService.createEducation(securityMember.getId(), requestDTO);
        return ApiResponse.success_only(SuccessStatus.CREATE_SPEC_SUCCESS);
    }

    @Operation(
            summary = "어학 입력 API (용범)", description = "입력= LanguageSkillRequestDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "스펙 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @PostMapping("/language")
    public ResponseEntity<ApiResponse<Void>> createLanguageSkill(@AuthenticationPrincipal SecurityMember securityMember,
                                                                 @Valid @RequestBody LanguageSkillRequestDTO requestDTO) {

        memberSpecificationService.createLanguageSkill(securityMember.getId(), requestDTO);
        return ApiResponse.success_only(SuccessStatus.CREATE_SPEC_SUCCESS);
    }

    @Operation(
            summary = "자격증 입력 API (용범)", description = "입력= CertificationRequestDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "스펙 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @PostMapping("/certification")
    public ResponseEntity<ApiResponse<Void>> createCertification(@AuthenticationPrincipal SecurityMember securityMember,
                                                                 @Valid @RequestBody CertificationRequestDTO requestDTO) {

        memberSpecificationService.createCertification(securityMember.getId(), requestDTO);
        return ApiResponse.success_only(SuccessStatus.CREATE_SPEC_SUCCESS);
    }

    @Operation(
            summary = "경력사항 입력 API (용범)", description = "입력= CareerRequestDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "스펙 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @PostMapping("/career")
    public ResponseEntity<ApiResponse<Void>> createCareer(@AuthenticationPrincipal SecurityMember securityMember,
                                                          @Valid @RequestBody CareerRequestDTO requestDTO) {

        memberSpecificationService.createCareer(securityMember.getId(), requestDTO);
        return ApiResponse.success_only(SuccessStatus.CREATE_SPEC_SUCCESS);
    }

    @Operation(
            summary = "수상 입력 API (용범)", description = "입력= AwardCreateDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "스펙 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @PostMapping("/award")
    public ResponseEntity<ApiResponse<Void>> createAward(@AuthenticationPrincipal SecurityMember securityMember,
                                                         @Valid @RequestBody AwardCreateDTO requestDTO) {

        memberSpecificationService.createAward(securityMember.getId(), requestDTO);
        return ApiResponse.success_only(SuccessStatus.CREATE_SPEC_SUCCESS);
    }

    @Operation(
            summary = "경험 입력 API (용범)", description = "입력= ExperienceRequestDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "스펙 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @PostMapping("/experience")
    public ResponseEntity<ApiResponse<Void>> createExperience(@AuthenticationPrincipal SecurityMember securityMember,
                                                              @Valid @RequestBody ExperienceRequestDTO requestDTO) {

        memberSpecificationService.createExperience(securityMember.getId(), requestDTO);
        return ApiResponse.success_only(SuccessStatus.CREATE_SPEC_SUCCESS);
    }

    /*
     * r
     * */

    @Operation(
            summary = "내 스펙 조회 API (용범)",
            description = "출력= MemberSpecificationRequestDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스펙 조회 성공"),
    })
    @GetMapping
    public ResponseEntity<ApiResponse<MemberSpecificationResponseDTO>> getMemberSpecification(@AuthenticationPrincipal SecurityMember securityMember) {

        MemberSpecificationResponseDTO response = memberSpecificationService.getMemberSpecification(securityMember.getId());

        return ApiResponse.success(SuccessStatus.GET_MEMBER_SPECIFICATION_SUCCESS, response);
    }

    /*
     * u
     * */

    @Operation(
            summary = "학력 수정 API (용범)", description = "입력= EducationRequestDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스펙 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @PutMapping("/education/{educationId}")
    public ResponseEntity<ApiResponse<Void>> updateEducation(@AuthenticationPrincipal SecurityMember securityMember,
                                                             @PathVariable("educationId") Long educationId,
                                                             @Valid @RequestBody EducationRequestDTO requestDTO) {

        memberSpecificationService.updateEducation(securityMember.getId(), educationId, requestDTO);
        return ApiResponse.success_only(SuccessStatus.UPDATE_SPEC_SUCCESS);
    }

    @Operation(
            summary = "어학 수정 API (용범)", description = "입력= LanguageSkillRequestDTO.LanguageSkill"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스펙 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @PutMapping("/language/{languageSkillId}")
    public ResponseEntity<ApiResponse<Void>> updateLanguageSkill(@AuthenticationPrincipal SecurityMember securityMember,
                                                                 @PathVariable("languageSkillId") Long languageSkillId,
                                                                 @Valid @RequestBody LanguageSkillRequestDTO.LanguageSkill requestDTO) {

        memberSpecificationService.updateLanguageSkill(securityMember.getId(), languageSkillId, requestDTO);
        return ApiResponse.success_only(SuccessStatus.UPDATE_SPEC_SUCCESS);
    }

    @Operation(
            summary = "자격증 수정 API (용범)", description = "입력= CertificationRequestDTO.Certification"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스펙 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @PutMapping("/certification/{certificationId}")
    public ResponseEntity<ApiResponse<Void>> updateCertification(@AuthenticationPrincipal SecurityMember securityMember,
                                                                 @PathVariable("certificationId") Long certificationId,
                                                                 @Valid @RequestBody CertificationRequestDTO.Certification requestDTO) {

        memberSpecificationService.updateCertification(securityMember.getId(), certificationId, requestDTO);
        return ApiResponse.success_only(SuccessStatus.UPDATE_SPEC_SUCCESS);
    }

    @Operation(
            summary = "경력사항 수정 API (용범)", description = "입력= CareerRequestDTO.Career"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스펙 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @PutMapping("/career/{careerId}")
    public ResponseEntity<ApiResponse<Void>> updateCareer(@AuthenticationPrincipal SecurityMember securityMember,
                                                          @PathVariable("careerId") Long careerId,
                                                          @Valid @RequestBody CareerRequestDTO.Career requestDTO) {

        memberSpecificationService.updateCareer(securityMember.getId(), careerId, requestDTO);
        return ApiResponse.success_only(SuccessStatus.UPDATE_SPEC_SUCCESS);
    }

    @Operation(
            summary = "수상 수정 API (용범)", description = "입력= AwardCreateDTO.Award"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스펙 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @PutMapping("/award/{awardId}")
    public ResponseEntity<ApiResponse<Void>> updateAward(@AuthenticationPrincipal SecurityMember securityMember,
                                                         @PathVariable("awardId") Long awardId,
                                                         @Valid @RequestBody AwardCreateDTO.Award requestDTO) {

        memberSpecificationService.updateAward(securityMember.getId(), awardId, requestDTO);
        return ApiResponse.success_only(SuccessStatus.UPDATE_SPEC_SUCCESS);
    }

    @Operation(
            summary = "경험 수정 API (용범)", description = "입력= ExperienceRequestDTO.Experience"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스펙 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @PutMapping("/experience/{experienceId}")
    public ResponseEntity<ApiResponse<Void>> updateExperience(@AuthenticationPrincipal SecurityMember securityMember,
                                                              @PathVariable("experienceId") Long experienceId,
                                                              @Valid @RequestBody ExperienceRequestDTO.Experience requestDTO) {

        memberSpecificationService.updateExperience(securityMember.getId(), experienceId, requestDTO);
        return ApiResponse.success_only(SuccessStatus.UPDATE_SPEC_SUCCESS);
    }


    /*
     * d
     * */

    @Operation(
            summary = "스펙 삭제 API (용범)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스펙 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteMemberSpecification(@AuthenticationPrincipal SecurityMember securityMember) {

        memberSpecificationService.deleteMemberSpecification(securityMember.getId());

        return ApiResponse.success_only(SuccessStatus.DELETE_SPEC_SUCCESS);
    }

    @Operation(
            summary = "학력 삭제 API (용범)", description = "입력: IdsDeleteRequestDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스펙 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "스펙에 접근할 권한이 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @DeleteMapping("/education")
    public ResponseEntity<ApiResponse<Void>> deleteEducation(@AuthenticationPrincipal SecurityMember securityMember,
                                                             @RequestBody IdsDeleteRequestDTO requestDTO) {

        memberSpecificationService.deleteEducation(securityMember.getId(), requestDTO);
        return ApiResponse.success_only(SuccessStatus.DELETE_SPEC_SUCCESS);
    }

    @Operation(
            summary = "어학 삭제 API (용범)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스펙 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "스펙에 접근할 권한이 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @DeleteMapping("/language")
    public ResponseEntity<ApiResponse<Void>> deleteLanguageSkill(@AuthenticationPrincipal SecurityMember securityMember,
                                                                 @RequestBody IdsDeleteRequestDTO requestDTO) {

        memberSpecificationService.deleteLanguageSkill(securityMember.getId(), requestDTO);
        return ApiResponse.success_only(SuccessStatus.DELETE_SPEC_SUCCESS);
    }


    @Operation(
            summary = "자격증 삭제 API (용범)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스펙 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "스펙에 접근할 권한이 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @DeleteMapping("/certification")
    public ResponseEntity<ApiResponse<Void>> deleteCertification(@AuthenticationPrincipal SecurityMember securityMember,
                                                                 @RequestBody IdsDeleteRequestDTO requestDTO) {

        memberSpecificationService.deleteCertification(securityMember.getId(), requestDTO);
        return ApiResponse.success_only(SuccessStatus.DELETE_SPEC_SUCCESS);
    }

    @Operation(
            summary = "경력 삭제 API (용범)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스펙 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "스펙에 접근할 권한이 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @DeleteMapping("/career")
    public ResponseEntity<ApiResponse<Void>> deleteCareer(@AuthenticationPrincipal SecurityMember securityMember,
                                                          @RequestBody IdsDeleteRequestDTO requestDTO) {

        memberSpecificationService.deleteCareer(securityMember.getId(), requestDTO);
        return ApiResponse.success_only(SuccessStatus.DELETE_SPEC_SUCCESS);
    }

    @Operation(
            summary = "수상 삭제 API (용범)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스펙 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "스펙에 접근할 권한이 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @DeleteMapping("/award")
    public ResponseEntity<ApiResponse<Void>> deleteAward(@AuthenticationPrincipal SecurityMember securityMember,
                                                         @RequestBody IdsDeleteRequestDTO requestDTO) {

        memberSpecificationService.deleteAward(securityMember.getId(), requestDTO);
        return ApiResponse.success_only(SuccessStatus.DELETE_SPEC_SUCCESS);
    }

    @Operation(
            summary = "경험 삭제 API (용범)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스펙 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "스펙에 접근할 권한이 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @DeleteMapping("/experience")
    public ResponseEntity<ApiResponse<Void>> deleteExperience(@AuthenticationPrincipal SecurityMember securityMember,
                                                              @RequestBody IdsDeleteRequestDTO requestDTO) {

        memberSpecificationService.deleteExperience(securityMember.getId(), requestDTO);
        return ApiResponse.success_only(SuccessStatus.DELETE_SPEC_SUCCESS);
    }



    /*
     * 스펙 평가
     * */

    /*
     * c
     * */

    @Operation(
            summary = "내 스펙 평가 API (용범)",
            description = "출력= 스펙 평가 id"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "스펙 평가 완료"),
    })
    @PostMapping("/evaluation")
    public ResponseEntity<ApiResponse<Long>> evaluate(@AuthenticationPrincipal SecurityMember securityMember) {

        Long response = memberSpecificationService.evaluateSpecification(securityMember.getId());

        return ApiResponse.success(SuccessStatus.SPEC_EVALUATION_SUCCESS, response);
    }

    /*
     * r
     * */

    @Operation(
            summary = "내 스펙 평가 조회 API (용범)",
            description = "출력= MemberSpecEvaluationResponseDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스펙 평가 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "본인 스펙 평가가 아닙니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 평가 정보를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스펙 정보를 찾을 수 없습니다."),
    })
    @GetMapping("/evaluation/{spec-evaluation-id}")
    public ResponseEntity<ApiResponse<MemberSpecEvaluationResponseDTO>> getSpecEvaluation(@AuthenticationPrincipal SecurityMember securityMember,
                                                                                          @PathVariable("spec-evaluation-id") Long specEvaluationId) {

        MemberSpecEvaluationResponseDTO response = memberSpecificationService.getSpecEvaluation(securityMember.getId(), specEvaluationId);

        return ApiResponse.success(SuccessStatus.SPEC_EVALUATION_FIND_SUCCESS, response);
    }
}
