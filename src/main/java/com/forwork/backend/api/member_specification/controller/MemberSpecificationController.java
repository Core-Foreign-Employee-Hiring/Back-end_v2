package com.forwork.backend.api.member_specification.controller;

import com.forwork.backend.api.member_specification.dto.response.MemberSpecEvaluationResponseDTO;
import com.forwork.backend.api.member_specification.dto.request.MemberSpecificationRequestDTO;
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

    @Operation(
            summary = "내 스펙 입력 API (용범)", description = "입력= MemberSpecificationRequestDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "스펙 등록 성공"),
    })
    @PostMapping
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
    @GetMapping
    public ResponseEntity<ApiResponse<MemberSpecificationResponseDTO>> getMemberSpecification(@AuthenticationPrincipal SecurityMember securityMember) {

        MemberSpecificationResponseDTO response = memberSpecificationService.getMemberSpecification(securityMember.getId());

        return ApiResponse.success(SuccessStatus.GET_MEMBER_SPECIFICATION_SUCCESS, response);
    }

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
                                                                                          @PathVariable("spec-evaluation-id")Long specEvaluationId) {

        MemberSpecEvaluationResponseDTO response = memberSpecificationService.getSpecEvaluation(securityMember.getId(), specEvaluationId);

        return ApiResponse.success(SuccessStatus.SPEC_EVALUATION_FIND_SUCCESS, response);
    }
}
