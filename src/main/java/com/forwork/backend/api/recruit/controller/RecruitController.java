package com.forwork.backend.api.recruit.controller;

import com.forwork.backend.api.member.entity.Nationality;
import com.forwork.backend.api.recruit.dto.request.RecruitRequestDTO;
import com.forwork.backend.api.recruit.dto.request.RecruitUpdateRequestDTO;
import com.forwork.backend.api.recruit.dto.response.RecruitBookmarkStatusResponseDTO;
import com.forwork.backend.api.recruit.dto.response.RecruitDetailResponseDTO;
import com.forwork.backend.api.recruit.dto.response.RecruitDraftResponseDTO;
import com.forwork.backend.api.recruit.dto.response.RecruitPreviewResponseDTO;
import com.forwork.backend.api.recruit.enums.ContractType;
import com.forwork.backend.api.recruit.enums.LanguageType;
import com.forwork.backend.api.recruit.enums.RecruitBookmarkStatus;
import com.forwork.backend.api.recruit.enums.WorkRegion;
import com.forwork.backend.api.recruit.service.RecruitService;
import com.forwork.backend.common.config.security.SecurityMember;
import com.forwork.backend.common.dto.PageResponseDTO;
import com.forwork.backend.api.member.entity.JobRole;
import com.forwork.backend.api.member.entity.Visa;
import com.forwork.backend.common.response.ApiResponse;
import com.forwork.backend.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "Recruit", description = "Recruit 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/recruit")
@Slf4j
@Validated
public class RecruitController {
    private final RecruitService recruitService;


    /*
    * c
    * */
    @Operation(
            summary = "공고 등록 API (용범)",
            description = "회사정보와 근무지 주소는 마이페이지 api 이용(해당 api 아직 미완)<b>" +
                    "입력: RecruitRequestDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "공고 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "해당 사용자를 찾을 수 없습니다."),
    })
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> save(@Valid @RequestBody RecruitRequestDTO recruitRequestDTO) {
        Long recruitId = recruitService.save(recruitRequestDTO);

        return ApiResponse.success_only(SuccessStatus.CREATE_RECRUIT_ARTICLE_SUCCESS);
    }

    /*
     * r
     * */

    @Operation(
            summary = "공고 상세 조회  API (용범)",
            description = "출력: RecruitDetailResponseDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "공고 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "해당 공고를 찾을 수 없습니다."),
    })
    @GetMapping("/{recruit-id}")
    public ResponseEntity<ApiResponse<RecruitDetailResponseDTO>> getRecruit(@AuthenticationPrincipal SecurityMember securityMember,
                                                                            @PathVariable("recruit-id") Long recruitId) {

        Long memberId=securityMember!=null?securityMember.getId():null;


        RecruitDetailResponseDTO response = recruitService.getRecruit(memberId, recruitId);

        return ApiResponse.success(SuccessStatus.SEND_RECRUIT_DETAIL_SUCCESS, response);
    }

    @Operation(
            summary = "공고 등록할 때, 임시 저장한 공고 조회(일단, 가장 최신 거 조회) API (용범)",
            description = "출력: RecruitDraftResponseDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "임시 저장된 공고 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "해당 공고를 찾을 수 없습니다."),
    })
    @GetMapping("/latest-draft")
    public ResponseEntity<ApiResponse<RecruitDraftResponseDTO>> getLatestDraft(@AuthenticationPrincipal SecurityMember securityMember) {
        RecruitDraftResponseDTO response = recruitService.getLatestDraft(securityMember.getId());

        return ApiResponse.success(SuccessStatus.SEND_DRAFT_SAVE_SUCCESS, response);
    }

    @Operation(
            summary = "공고 전체 조회  API (용범)",
            description = "출력: RecruitPreviewResponseDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "공고 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "직무는 최대 5개까지 선택할 수 있습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "언어는 최대 5개까지 선택할 수 있습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "근무 지역은 최대 3개까지 선택할 수 있습니다."),
    })
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<RecruitPreviewResponseDTO>>> getRecruits(
            @Parameter(description = "검색 키워드", in = ParameterIn.QUERY)
            @RequestParam(value = "keyword", required = false) String keyword,

            @Parameter(description = "직무", in = ParameterIn.QUERY)
            @RequestParam(value = "jobRoles", required = false) Set<JobRole> jobRoles,

            @Parameter(description = "관련 국적", in = ParameterIn.QUERY)
            @RequestParam(value = "nationality", required = false) Nationality nationality,

            @Parameter(description = "언어", in = ParameterIn.QUERY)
            @RequestParam(value = "languageTypes", required = false) Set<LanguageType> languageTypes,

            @Parameter(description = "비자", in = ParameterIn.QUERY)
            @RequestParam(value = "visa", required = false) Visa visa,

            @Parameter(description = "근무지역", in = ParameterIn.QUERY)
            @RequestParam(value = "workRegions", required = false) Set<WorkRegion> workRegions,

            @Parameter(description = "계약 형태", in = ParameterIn.QUERY)
            @RequestParam(value = "contractType", required = false) ContractType contractType,


            @Parameter(description = "페이지 번호 (0부터 시작)", in = ParameterIn.QUERY)
            @RequestParam(value = "page", defaultValue = "0") Integer page,

            @Parameter(description = "페이지 크기", in = ParameterIn.QUERY)
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {

        PageResponseDTO<RecruitPreviewResponseDTO> response = recruitService.getRecruits(keyword, page, size, jobRoles, nationality, languageTypes, visa, workRegions, contractType);
        return ApiResponse.success(SuccessStatus.SEND_RECRUIT_ALL_LIST_SUCCESS, response);
    }

    /*
     * u
     * */

    @Operation(
            summary = "공고 수정  API (용범)",
            description = "입력: RecruitUpdateRequestDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "공고 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "해당 공고에 대한 권한이 없습니다.")
    })
    @PatchMapping("/{recruit-id}")
    public ResponseEntity<ApiResponse<Void>> getRecruits(@RequestBody RecruitUpdateRequestDTO recruitUpdateRequestDTO,
                                                         @PathVariable("recruit-id") Long recruitId) {
        recruitService.updateRecruit(recruitId, recruitUpdateRequestDTO);
        return ApiResponse.success_only(SuccessStatus.RECRUIT_UPDATE_SUCCESS);
    }


    @Operation(summary = "공고 찜하기 상태 변경. API (용범)",
            description = "공고 찜하기 상태 변경.<br>" +
                    "변경 후 상태 리턴."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "찜하기 상태 변경 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @PatchMapping(value = "/{recruit-id}/bookmark")
    public ResponseEntity<ApiResponse<RecruitBookmarkStatusResponseDTO>> flipRecruitBookmark(@AuthenticationPrincipal SecurityMember securityMember,
                                                                                             @PathVariable("recruit-id") Long recruitId) {

        RecruitBookmarkStatus recruitBookmarkStatus = recruitService.flipRecruitBookmark(securityMember.getId(), recruitId);
        RecruitBookmarkStatusResponseDTO response = new RecruitBookmarkStatusResponseDTO(recruitBookmarkStatus);
        return ApiResponse.success(SuccessStatus.UPDATE_RECRUIT_BOOKMARK_STATUS_SUCCESS, response);
    }


    /*
     * d
     * */

    @Operation(
            summary = "공고 삭제  API (용범)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "공고 삭제 성공"),
    })
    @DeleteMapping("/{recruit-id}")
    public ResponseEntity<ApiResponse<Void>> deleteRecruit(@PathVariable("recruit-id") Long recruitId) {
        recruitService.deleteRecruit(recruitId);
        return ApiResponse.success_only(SuccessStatus.DELETE_RECRUIT_ARTICLE_SUCCESS);
    }
}
