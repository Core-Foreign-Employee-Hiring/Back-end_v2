package com.forwork.backend.api.recruit_review.controller;

import com.forwork.backend.api.recruit_review.dto.request.RecruitReviewCommentCreateDTO;
import com.forwork.backend.api.recruit_review.dto.request.RecruitReviewCommentUpdateDTO;
import com.forwork.backend.api.recruit_review.dto.request.RecruitReviewCreateDTO;
import com.forwork.backend.api.recruit_review.dto.request.RecruitReviewUpdateDTO;
import com.forwork.backend.api.recruit_review.dto.response.RecruitReviewDetailResponseDTO;
import com.forwork.backend.api.recruit_review.dto.response.RecruitReviewParentCommentResponseDTO;
import com.forwork.backend.api.recruit_review.dto.response.RecruitReviewPreviewResponseDTO;
import com.forwork.backend.api.recruit_review.dto.response.RecruitReviewTotalCountResponseDTO;
import com.forwork.backend.api.recruit_review.enums.RecruitReviewSortType;
import com.forwork.backend.api.recruit_review.service.RecruitReviewCommentService;
import com.forwork.backend.api.recruit_review.service.RecruitReviewService;
import com.forwork.backend.common.config.security.SecurityMember;
import com.forwork.backend.common.dto.PageResponseDTO;
import com.forwork.backend.common.response.ApiResponse;
import com.forwork.backend.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "RecruitReview", description = "RecruitReview 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/recruit-review")
@Slf4j
public class RecruitReviewController {
    private final RecruitReviewService recruitReviewService;
    private final RecruitReviewCommentService recruitReviewCommentService;



    /*
     * c(후기)
     * */
    @Operation(
            summary = "후기 등록 API (용범)",
            description = "입력: RecruitReviewCreateDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "후기 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없습니다."),
    })
    @PostMapping("/")
    public ResponseEntity<ApiResponse<Void>> save(@AuthenticationPrincipal SecurityMember securityMember,
                                                  @RequestBody RecruitReviewCreateDTO dto) {
        recruitReviewService.createRecruitReview(securityMember.getId(), dto);

        return ApiResponse.success_only(SuccessStatus.RECRUIT_REVIEW_CREATE_SUCCESS);
    }

    /*
     * r(후기)
     * */

    @Operation(
            summary = "후기 상세 조회  API (용범)",
            description = "출력: RecruitReviewDetailResponseDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "후기 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 후기를 찾을 수 없습니다."),
    })
    @GetMapping("/{recruit-review-id}")
    public ResponseEntity<ApiResponse<RecruitReviewDetailResponseDTO>> getRecruit(@AuthenticationPrincipal SecurityMember securityMember,
                                                                                  @PathVariable("recruit-review-id") Long recruitReviewId) {

        Long memberId=securityMember!=null?securityMember.getId():-1;

        RecruitReviewDetailResponseDTO response = recruitReviewService.getRecruitReview(memberId, recruitReviewId);


        return ApiResponse.success(SuccessStatus.RECRUIT_REVIEW_DETAIL_SUCCESS, response);
    }

    @Operation(
            summary = "후기 전체 조회 API (용범)",
            description = "출력: RecruitReviewPreviewInternalDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "후기 조회 성공"),
    })
    @GetMapping("/")
    public ResponseEntity<ApiResponse<PageResponseDTO<RecruitReviewPreviewResponseDTO>>> getRecruits(
            @Parameter(description = "검색 키워드", in = ParameterIn.QUERY)
            @RequestParam(value = "keyword", required = false) String keyword,

            @Parameter(description = "정렬 타입 LATEST(최신순), MOST_VIEWED(조회순)", in = ParameterIn.QUERY)
            @RequestParam(value = "sortType", defaultValue = "LATEST") RecruitReviewSortType sortType,

            @Parameter(description = "페이지 번호 (0부터 시작)", in = ParameterIn.QUERY)
            @RequestParam(value = "page", defaultValue = "0") Integer page,

            @Parameter(description = "페이지 크기", in = ParameterIn.QUERY)
            @RequestParam(value = "size", defaultValue = "10") Integer size) {


        PageResponseDTO<RecruitReviewPreviewResponseDTO> response = recruitReviewService.getRecruitPreviews(keyword, page, size, sortType);

        return ApiResponse.success(SuccessStatus.RECRUIT_REVIEW_LIST_SUCCESS, response);
    }

    @Operation(
            summary = "후기 totalCount 조회  API (용범)",
            description = "출력: RecruitReviewTotalCountResponseDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "후기 총 개수 조회 성공"),
    })
    @GetMapping("/total-count")
    public ResponseEntity<ApiResponse<RecruitReviewTotalCountResponseDTO>> getRecruit() {

        RecruitReviewTotalCountResponseDTO response = recruitReviewService.getRecruitReviewTotalCount();

        return ApiResponse.success(SuccessStatus.RECRUIT_REVIEW_TOTAL_COUNT_SUCCESS, response);
    }


    /*
    * u(후기)
    * */

    @Operation(
            summary = "후기 수정  API (용범)",
            description = "입력: RecruitReviewUpdateDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "공고 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "후기에 대한 권한이 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "후기를 찾을 수 없습니다.")
    })
    @PatchMapping("/{recruit-review-id}")
    public ResponseEntity<ApiResponse<Void>> getRecruits(@AuthenticationPrincipal SecurityMember securityMember,
                                                         @RequestBody RecruitReviewUpdateDTO recruitReviewUpdateDTO,
                                                         @PathVariable("recruit-review-id") Long recruitReviewId) {

        recruitReviewService.updateRecruitReview(securityMember.getId(), recruitReviewId, recruitReviewUpdateDTO);

        return ApiResponse.success_only(SuccessStatus.RECRUIT_REVIEW_UPDATE_SUCCESS);
    }

    /*
    * d(후기)
    * */

    @Operation(
            summary = "후기 삭제  API (용범)",
            description = ""
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "채용 후기 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "해당 후기에 대한 권한이 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 후기를 찾을 수 없습니다.")
    })
    @DeleteMapping("/{recruit-review-id}")
    public ResponseEntity<ApiResponse<Void>> deleteRecruitReview(@AuthenticationPrincipal SecurityMember securityMember,
                                                                 @PathVariable("recruit-review-id") Long recruitReviewId) {
        recruitReviewService.deleteRecruitReview(securityMember.getId(), recruitReviewId);

        return ApiResponse.success_only(SuccessStatus.RECRUIT_REVIEW_DELETE_SUCCESS);
    }


    /*
     * c(댓글)
     * */
    @Operation(
            summary = "댓글 등록 API (용범)",
            description = "입력: RecruitReviewCommentCreateDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "댓글 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없습니다. 채용 후기를 찾을 수 없습니다.")
    })
    @PostMapping("/{recruit-review-id}/comments")
    public ResponseEntity<ApiResponse<Void>> createComment(@AuthenticationPrincipal SecurityMember securityMember,
                                                           @PathVariable("recruit-review-id") Long recruitReviewId,
                                                           @RequestBody RecruitReviewCommentCreateDTO dto) {

        recruitReviewCommentService.createRecruitReviewComment(securityMember.getId(), recruitReviewId, dto);

        return ApiResponse.success_only(SuccessStatus.RECRUIT_REVIEW_COMMENT_CREATE_SUCCESS);
    }


    /*
    * r(댓글)
    * */

    @Operation(
            summary = "채용 후기 댓글 조회 API (용범)",
            description = "출력: RecruitReviewParentCommentResponseDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "댓글 조회 성공"),
    })
    @GetMapping("/{recruit-review-id}/comments")
    public ResponseEntity<ApiResponse<List<RecruitReviewParentCommentResponseDTO>>> getComments(@AuthenticationPrincipal SecurityMember securityMember,
                                                                                                @PathVariable("recruit-review-id") Long recruitReviewId) {

        List<RecruitReviewParentCommentResponseDTO> response = recruitReviewCommentService.getComments(securityMember.getId(), recruitReviewId);

        return ApiResponse.success(SuccessStatus.SEND_RECRUIT_REVIEW_COMMENT_SUCCESS, response);
    }


    /*
     * u(댓글)
     * */

    @Operation(
            summary = "댓글 수정  API (용범)",
            description = "입력: RecruitReviewCommentUpdateDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "해당 후기에 대한 권한이 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 댓글을 찾을 수 없습니다.")
    })
    @PatchMapping("/comments/{comment-id}")
    public ResponseEntity<ApiResponse<Void>> getRecruits(@AuthenticationPrincipal SecurityMember securityMember,
                                                         @RequestBody RecruitReviewCommentUpdateDTO recruitReviewCommentUpdateDTO,
                                                         @PathVariable("comment-id") Long commentId) {
        recruitReviewCommentService.updateRecruitReviewComment(securityMember.getId(), commentId, recruitReviewCommentUpdateDTO);

        return ApiResponse.success_only(SuccessStatus.RECRUIT_REVIEW_COMMENT_UPDATE_SUCCESS);
    }

    /*
     * d(후기)
     * */

    @Operation(
            summary = "댓글 삭제 API (용범)",
            description = ""
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "채용 후기 댓글 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "해당 댓글에 대한 권한이 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 댓글을 찾을 수 없습니다.")
    })
    @DeleteMapping("/comments/{comment-id}")
    public ResponseEntity<ApiResponse<Void>> deleteRecruitReviewComment(@AuthenticationPrincipal SecurityMember securityMember,
                                                                        @PathVariable("comment-id") Long commentId) {
        recruitReviewCommentService.deleteRecruitReviewComment(securityMember.getId(), commentId);

        return ApiResponse.success_only(SuccessStatus.RECRUIT_REVIEW_COMMENT_DELETE_SUCCESS);
    }

}
