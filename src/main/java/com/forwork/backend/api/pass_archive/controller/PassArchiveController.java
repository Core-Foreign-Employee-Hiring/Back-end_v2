package com.forwork.backend.api.pass_archive.controller;

import com.forwork.backend.api.pass_archive.dto.*;
import com.forwork.backend.api.pass_archive.service.ArchiveInquiryService;
import com.forwork.backend.api.pass_archive.service.ArchiveReviewService;
import com.forwork.backend.api.pass_archive.service.ArchiveTestService;
import com.forwork.backend.api.pass_archive.service.PassArchiveService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "PassArchive", description = "합격 아카이브 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/pass-archives")
@RequiredArgsConstructor
public class PassArchiveController {
    private final PassArchiveService passArchiveService;
    private final ArchiveReviewService archiveReviewService;
    private final ArchiveInquiryService archiveInquiryService;
    private final ArchiveTestService archiveTestService;

    @Operation(summary = "합격아카이브 등록 (태근)", description = "무료일 경우 price를 0으로 넘겨주세요 / thumbnail : 썸네일 , images : 본문 이미지들, products : 판매할 상품들")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "합격 아카이브 등록 성공"),
    })
    @PostMapping(
            value = "",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<Long>> create(
            @RequestPart("data") PassArchiveCreateRequestDTO passArchiveCreateRequestDTO,
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "products") List<MultipartFile> products,
            @AuthenticationPrincipal SecurityMember securityMember) {

        Long id = passArchiveService.createArchive(passArchiveCreateRequestDTO, thumbnail, images, products, securityMember.getId());

        return ApiResponse.success(SuccessStatus.CREATE_PASS_ARCHIVE_SUCCESS, id);
    }

    @Operation(summary = "합격아카이브 상세 조회 (태근)", description = "ID로 합격아카이브 상세 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "합격 아카이브 상세 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "합격 아카이브를 찾을 수 없습니다.")
    })
    @GetMapping("/detail")
    public ResponseEntity<ApiResponse<PassArchiveDetailResponseDTO>> getDetail(@AuthenticationPrincipal SecurityMember securityMember,
                                                                               @RequestParam("id") Long id) {

        Long memberId = (securityMember==null)?null: securityMember.getId();

        PassArchiveDetailResponseDTO passArchiveDetailResponseDTO = passArchiveService.getDetailArchive(memberId, id);
        return ApiResponse.success(SuccessStatus.SEND_PASS_ARCHIVE_DETAIL_SUCCESS, passArchiveDetailResponseDTO);
    }

    @Operation(summary = "아카이브 다운 (용범)", description = "구매한 아카이브 다운.<b>" +
            "출력: PassArchiveFileResponseDTO")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "아카이브 다운로드 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "해당 아카이브에 대한 구매 내역이 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "합격 아카이브를 찾을 수 없습니다.")
    })
    @GetMapping("/{archive-id}/download")
    public ResponseEntity<ApiResponse<List<PassArchiveFileResponseDTO>>> downloadArchive(@AuthenticationPrincipal SecurityMember securityMember,
                                                                                         @PathVariable("archive-id") Long archiveId) {

        List<PassArchiveFileResponseDTO> response = passArchiveService.downloadArchive(securityMember.getId(), archiveId);

        return ApiResponse.success(SuccessStatus.DOWNLOAD_PASS_ARCHIVE_SUCCESS, response);
    }


    @Operation(
            summary = "아카이브 리뷰 등록 API (용범)", description = "입력: ArchiveReviewRequestDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "아카이브 리뷰 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미 리뷰를 작성하셨습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "합격 아카이브를 찾을 수 없습니다."),
    })
    @PostMapping("/{pass-archive-id}/reviews")
    public ResponseEntity<ApiResponse<Void>> save(@AuthenticationPrincipal SecurityMember securityMember,
                                                  @PathVariable("pass-archive-id")Long archiveId,
                                                  @RequestBody ArchiveReviewRequestDTO dto) {
        archiveReviewService.createArchiveReview(securityMember.getId(), archiveId, dto);

        return ApiResponse.success_only(SuccessStatus.ARCHIVE_REVIEW_CREATE_SUCCESS);
    }


    @Operation(
            summary = "아카이브 리뷰 조회 API (용범)", description = "출력: ArchiveReviewResponseDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "아카이브 리뷰 조회 성공"),
    })
    @GetMapping("/{pass-archive-id}/reviews")
    public ResponseEntity<ApiResponse<PageResponseDTO<ArchiveReviewResponseDTO>>> getArchiveReviews(
            @AuthenticationPrincipal SecurityMember securityMember,
            @PathVariable("pass-archive-id") Long archiveId,

            @Parameter(description = "페이지 번호 (0부터 시작)", in = ParameterIn.QUERY)
            @RequestParam(value = "page", defaultValue = "0") Integer page,

            @Parameter(description = "페이지 크기", in = ParameterIn.QUERY)
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        PageResponseDTO<ArchiveReviewResponseDTO> response = archiveReviewService.getArchiveReviews(archiveId, page, size);

        return ApiResponse.success(SuccessStatus.SEND_ARCHIVE_REVIEW_SUCCESS, response);
    }


    @Operation(
            summary = "아카이브 문의하기 API (용범)", description = "입력: ArchiveInquiryRequestDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "문의하기 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "합격 아카이브를 찾을 수 없습니다."),
    })
    @PostMapping("/{pass-archive-id}/inquiries")
    public ResponseEntity<ApiResponse<Void>> inquiry(@AuthenticationPrincipal SecurityMember securityMember, @PathVariable("pass-archive-id") Long archiveId,
                                                     @RequestBody ArchiveInquiryRequestDTO dto) {

        archiveInquiryService.inquiry(securityMember.getId(), archiveId, dto.inquiry());

        return ApiResponse.success_only(SuccessStatus.INQUIRY_CREATE_SUCCESS);
    }

    @Operation(
            summary = "아카이브 문의 답변하기 API (용범)", description = "입력: ArchiveInquiryAnswerRequestDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "답변하기 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "다른 사람 아카이브 문의에는 답변할 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미 답변이 등록되어 있습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "다른 사람 아카이브 문의에는 답변할 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "문의글을 찾을 수 없습니다."),
    })
    @PostMapping("/inquiries/{inquiry-id}/answers")
    public ResponseEntity<ApiResponse<Void>> answer(@AuthenticationPrincipal SecurityMember securityMember, @PathVariable("inquiry-id") Long inquiryId,
                                                    @RequestBody ArchiveInquiryAnswerRequestDTO dto) {
        archiveInquiryService.answer(securityMember.getId(), inquiryId, dto.answer());

        return ApiResponse.success_only(SuccessStatus.ANSWER_CREATE_SUCCESS);
    }

    @Operation(
            summary = "아카이브 문의 조회 API (용범)", description = "출력: ArchiveInquiryResponseDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "문의 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "문의글을 찾을 수 없습니다."),
    })
    @GetMapping("/inquiries/{inquiry-id}")
    public ResponseEntity<ApiResponse<ArchiveInquiryResponseDTO>> getInquiry(@AuthenticationPrincipal SecurityMember securityMember,
                                                                             @PathVariable("inquiry-id") Long inquiryId) {
        ArchiveInquiryResponseDTO response = archiveInquiryService.getInquiry(securityMember.getId(), inquiryId);

        return ApiResponse.success(SuccessStatus.SEND_INQUIRY_SUCCESS, response);
    }

    @Operation(
            summary = "아카이브 전체 조회  API (용범)",
            description = "출력: PassArchivePreviewResponseDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "합격 아카이브 전체 조회 성공"),
    })
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<PassArchivePreviewResponseDTO>>> getPassArchives(
            @Parameter(description = "검색 키워드", in = ParameterIn.QUERY)
            @RequestParam(value = "keyword", required = false) String keyword,

            @Parameter(description = "페이지 번호 (0부터 시작)", in = ParameterIn.QUERY)
            @RequestParam(value = "page", defaultValue = "0") Integer page,

            @Parameter(description = "페이지 크기", in = ParameterIn.QUERY)
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        PageResponseDTO<PassArchivePreviewResponseDTO> response = passArchiveService.getPassArchives(keyword, page, size);
        return ApiResponse.success(SuccessStatus.SEND_PASS_ARCHIVE_ALL_SUCCESS, response);
    }

    @Operation(
            summary = "문의하기 답변 달렸는지 확인. API (용범)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "문의 답변 유무 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "문의글을 찾을 수 없습니다."),
    })
    @GetMapping("/inquiries/{inquiry-id}/is-answered")
    public ResponseEntity<ApiResponse<Boolean>> isAnswered(@AuthenticationPrincipal SecurityMember securityMember,
                                                           @PathVariable("inquiry-id") Long inquiryId) {
        boolean answered = archiveInquiryService.isAnswered(inquiryId);

        return ApiResponse.success(SuccessStatus.CHECK_INQUIRY_ANSWERED_SUCCESS, answered);
    }

    @Operation(
            summary = "내가 보낸 문의 중 가장 최근 거 조회. API (용범)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내가 보낸 최근 문의 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "문의글을 찾을 수 없습니다."),
    })
    @GetMapping("/latest-inquiry")
    public ResponseEntity<ApiResponse<LatestInquiryResponseDTO>> getLatestInquiry(@AuthenticationPrincipal SecurityMember securityMember) {
        LatestInquiryResponseDTO response = archiveInquiryService.getLatestInquiry(securityMember.getId());

        return ApiResponse.success(SuccessStatus.GET_LATEST_MY_INQUIRY_SUCCESS, response);
    }

    @Operation(
            summary = "새로운 문의가 있나?. API (용범)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "특정 아카이브 읽지 않은 문의 조회 성공"),
    })
    @GetMapping("/{pass-archive-id}/inquiries/unread")
    public ResponseEntity<ApiResponse<Boolean>> hasUnreadInquiryForArchive(@AuthenticationPrincipal SecurityMember securityMember,
                                                                           @PathVariable("pass-archive-id") Long archiveId) {
        boolean response = archiveInquiryService.hasUnreadInquiryForArchive(archiveId);

        return ApiResponse.success(SuccessStatus.CHECK_UNREAD_INQUIRY_SUCCESS, response);
    }

    /*
    * 테스트
    * */

    @Operation(
            summary = "테스트: 결제 없이 아카이브 리뷰 등록 API (용범)", description = "입력: ArchiveReviewRequestDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "아카이브 리뷰 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미 리뷰를 작성하셨습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "합격 아카이브를 찾을 수 없습니다."),
    })
    @PostMapping("/test/{pass-archive-id}/reviews")
    public ResponseEntity<ApiResponse<Void>> test_save(@AuthenticationPrincipal SecurityMember securityMember,
                                                       @PathVariable("pass-archive-id") Long archiveId,
                                                       @RequestBody ArchiveReviewRequestDTO dto) {
        archiveTestService.createArchiveReview(securityMember.getId(), archiveId, dto);

        return ApiResponse.success_only(SuccessStatus.ARCHIVE_REVIEW_CREATE_SUCCESS);
    }
}