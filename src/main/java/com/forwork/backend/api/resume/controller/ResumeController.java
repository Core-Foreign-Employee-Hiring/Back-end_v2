package com.forwork.backend.api.resume.controller;

import com.forwork.backend.api.resume.dto.ResumeCreateRequest;
import com.forwork.backend.api.resume.dto.ResumeCreateResponse;
import com.forwork.backend.api.resume.dto.ResumeDetailResponse;
import com.forwork.backend.api.resume.dto.ResumeListResponse;
import com.forwork.backend.api.resume.dto.ResumeSelectionRequest;
import com.forwork.backend.api.resume.service.ResumeService;
import com.forwork.backend.common.config.security.SecurityMember;
import com.forwork.backend.common.dto.PageResponseDTO;
import com.forwork.backend.common.response.ApiResponse;
import com.forwork.backend.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Resume", description = "이력서 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/resumes")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;

    @Operation(
            summary = "이력서 초안 생성 API (1페이지) (태근)",
            description = "이력서 이름(필수), 프로필 이미지(필수), 자기소개(선택), URL 리스트(선택)를 받아 이력서를 생성합니다. " +
                    "입력된 필수/선택값은 자동으로 체크됩니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "이력서가 생성되었습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "프로필 이미지는 필수입니다. / 이미지 업로드에 실패했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없습니다.")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ResumeCreateResponse>> createResume(
            @AuthenticationPrincipal SecurityMember securityMember,
            @RequestPart("request") @Valid ResumeCreateRequest resumeCreateRequest,
            @RequestPart("profileImage") MultipartFile profileImage
    ) {
        ResumeCreateResponse resumeCreateResponse = resumeService.createResume(
                securityMember.getId(),
                resumeCreateRequest,
                profileImage
        );
        return ApiResponse.success(SuccessStatus.RESUME_CREATE_SUCCESS, resumeCreateResponse);
    }

    @Operation(
            summary = "내 이력서 목록 조회 API (태근)",
            description = "내가 작성한 이력서 목록을 페이지네이션하여 조회합니다. " +
                    "이력서 ID, 이력서 이름, 생성일자, 수정일자를 반환합니다. " +
                    "최신 수정일 기준 내림차순으로 정렬됩니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이력서 목록 조회 성공")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<ResumeListResponse>>> getMyResumes(
            @AuthenticationPrincipal SecurityMember securityMember,
            @Parameter(description = "페이지 번호 (0부터 시작)", in = ParameterIn.QUERY)
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @Parameter(description = "페이지 크기", in = ParameterIn.QUERY)
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<ResumeListResponse> resumes = resumeService.getMyResumes(securityMember.getId(), pageable);

        PageResponseDTO<ResumeListResponse> pageResponseDTO = PageResponseDTO.<ResumeListResponse>builder()
                .content(resumes.getContent())
                .page(resumes.getNumber())
                .size(resumes.getSize())
                .totalElements(resumes.getTotalElements())
                .totalPages(resumes.getTotalPages())
                .build();

        return ApiResponse.success(SuccessStatus.SEND_MY_RESUME_SUCCESS, pageResponseDTO);
    }

    @Operation(
            summary = "이력서 상세 조회 API (태근)",
            description = "이력서 ID로 이력서의 모든 정보를 조회합니다. " +
                    "회원 기본 정보와 선택된 스펙 정보들(학력, 자격증, 어학능력, 경력, 수상, 기타활동, URL)을 포함합니다. " +
                    "각 항목은 include 플래그가 true인 경우에만 데이터가 포함됩니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이력서 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "본인의 이력서만 수정/삭제할 수 있습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 이력서를 찾을 수 없습니다.")
    })
    @GetMapping("/{resumeId}")
    public ResponseEntity<ApiResponse<ResumeDetailResponse>> getResume(
            @AuthenticationPrincipal SecurityMember securityMember,
            @PathVariable Long resumeId
    ) {
        ResumeDetailResponse resumeDetailResponse = resumeService.getResume(resumeId, securityMember.getId());
        return ApiResponse.success(SuccessStatus.SEND_MY_RESUME_SUCCESS, resumeDetailResponse);
    }

    @Operation(
            summary = "이력서 항목 선택 업데이트 API (3페이지) (태근)",
            description = "스펙에서 입력받은 정보를 이력서에 포함할지 선택합니다. " +
                    "자기소개, 학력, 자격증, 어학능력, 경력, 수상, 기타활동, URL 항목을 선택할 수 있습니다. " +
                    "프로필 이미지는 필수이므로 선택 항목에서 제외됩니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이력서 항목이 업데이트되었습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "본인의 이력서만 수정/삭제할 수 있습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 이력서를 찾을 수 없습니다.")
    })
    @PatchMapping("/{resumeId}/selections")
    public ResponseEntity<ApiResponse<Void>> updateSelections(
            @AuthenticationPrincipal SecurityMember securityMember,
            @PathVariable Long resumeId,
            @RequestBody @Valid ResumeSelectionRequest resumeSelectionRequest
    ) {
        resumeService.updateResumeSelections(securityMember.getId(), resumeId, resumeSelectionRequest);
        return ApiResponse.success_only(SuccessStatus.RESUME_SELECTION_UPDATE_SUCCESS);
    }

    @Operation(
            summary = "이력서 삭제 API (태근)",
            description = "이력서와 관련된 프로필 이미지, URL을 모두 삭제합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이력서가 삭제되었습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "본인의 이력서만 수정/삭제할 수 있습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 이력서를 찾을 수 없습니다.")
    })
    @DeleteMapping("/{resumeId}")
    public ResponseEntity<ApiResponse<Void>> deleteResume(
            @AuthenticationPrincipal SecurityMember securityMember,
            @PathVariable Long resumeId
    ) {
        resumeService.deleteResume(resumeId, securityMember.getId());
        return ApiResponse.success_only(SuccessStatus.RESUME_DELETE_SUCCESS);
    }
}
