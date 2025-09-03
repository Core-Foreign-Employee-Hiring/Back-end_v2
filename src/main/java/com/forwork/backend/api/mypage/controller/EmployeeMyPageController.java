package com.forwork.backend.api.mypage.controller;

import com.forwork.backend.api.mypage.dto.response.ArchiveInquiryResponseDTO;
import com.forwork.backend.api.mypage.dto.response.PurchasedArchivesPreviewResponseDTO;
import com.forwork.backend.api.mypage.dto.response.SoldArchiveResponseDTO;
import com.forwork.backend.api.mypage.dto.response.WrittenArchiveResponseDTO;
import com.forwork.backend.api.mypage.service.EmployeeMyPageService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MyPage", description = "MyPage 관련 API 입니다.")
@RestController
@RequestMapping("/api/v2/my")
@RequiredArgsConstructor
public class EmployeeMyPageController {
    private final EmployeeMyPageService employeeMyPageService;

    /*
    * r
    * */

    @Operation(summary = "내가 구매한 아카이브 조회 (용범)", description = "출력= PurchasedArchivesPreviewResponseDTO")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "구매한 아카이브 조회 성공"),
    })
    @GetMapping("/purchased-archives")
    public ResponseEntity<ApiResponse<PageResponseDTO<PurchasedArchivesPreviewResponseDTO>>> createOrder(
            @AuthenticationPrincipal SecurityMember securityMember,

            @Parameter(description = "페이지 번호 (0부터 시작)", in = ParameterIn.QUERY)
            @RequestParam(value = "page", defaultValue = "0") Integer page,

            @Parameter(description = "페이지 크기", in = ParameterIn.QUERY)
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {

        PageResponseDTO<PurchasedArchivesPreviewResponseDTO> response = employeeMyPageService.getPurchasedArchives(securityMember.getId(), page, size);
        return ApiResponse.success(SuccessStatus.SEND_PURCHASED_ARCHIVES_SUCCESS, response);
    }

    @Operation(summary = "내가 보낸 문의 조회 (용범)", description = "출력: ArchiveInquiryResponseDTO")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내가 보낸 문의 조회 성공"),
    })
    @GetMapping("/inquiries/sent")
    public ResponseEntity<ApiResponse<PageResponseDTO<ArchiveInquiryResponseDTO>>> getSentInquiries(
            @AuthenticationPrincipal SecurityMember securityMember,

            @Parameter(description = "페이지 번호 (0부터 시작)", in = ParameterIn.QUERY)
            @RequestParam(value = "page", defaultValue = "0") Integer page,

            @Parameter(description = "페이지 크기", in = ParameterIn.QUERY)
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {

        PageResponseDTO<ArchiveInquiryResponseDTO> response = employeeMyPageService.getSentInquiries(securityMember.getId(), page, size);

        return ApiResponse.success(SuccessStatus.SEND_SENT_INQUIRIES_SUCCESS, response);
    }

    @Operation(summary = "내가 받은 문의 조회 (용범)", description = "출력: ArchiveInquiryResponseDTO")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내가 받은 문의 조회 성공"),
    })
    @GetMapping("/inquiries/received")
    public ResponseEntity<ApiResponse<PageResponseDTO<ArchiveInquiryResponseDTO>>> getReceivedInquiries(
            @AuthenticationPrincipal SecurityMember securityMember,

            @Parameter(description = "페이지 번호 (0부터 시작)", in = ParameterIn.QUERY)
            @RequestParam(value = "page", defaultValue = "0") Integer page,

            @Parameter(description = "페이지 크기", in = ParameterIn.QUERY)
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {

        PageResponseDTO<ArchiveInquiryResponseDTO> response = employeeMyPageService.getReceivedInquiries(securityMember.getId(), page, size);

        return ApiResponse.success(SuccessStatus.SEND_RECEIVED_INQUIRIES_SUCCESS, response);
    }

    @Operation(summary = "작성한 아카이브 조회 (용범)", description = "출력: WrittenArchiveResponseDTO")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "작성한 아카이브 조회 성공"),
    })
    @GetMapping("/archives")
    public ResponseEntity<ApiResponse<PageResponseDTO<WrittenArchiveResponseDTO>>> getWrittenArchives(
            @AuthenticationPrincipal SecurityMember securityMember,

            @Parameter(description = "페이지 번호 (0부터 시작)", in = ParameterIn.QUERY)
            @RequestParam(value = "page", defaultValue = "0") Integer page,

            @Parameter(description = "페이지 크기", in = ParameterIn.QUERY)
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {

        PageResponseDTO<WrittenArchiveResponseDTO> response = employeeMyPageService.getWrittenArchives(securityMember.getId(), page, size);

        return ApiResponse.success(SuccessStatus.GET_WRITTEN_ARCHIVE_SUCCESS, response);
    }

    @Operation(summary = "판매한 아카이브 조회 (용범)", description = "출력: SoldArchiveResponseDTO")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "판매한 아카이브 조회 성공"),
    })
    @GetMapping("/archives/sold")
    public ResponseEntity<ApiResponse<PageResponseDTO<SoldArchiveResponseDTO>>> getSoldArchives(
            @AuthenticationPrincipal SecurityMember securityMember,

            @Parameter(description = "페이지 번호 (0부터 시작)", in = ParameterIn.QUERY)
            @RequestParam(value = "page", defaultValue = "0") Integer page,

            @Parameter(description = "페이지 크기", in = ParameterIn.QUERY)
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {

        PageResponseDTO<SoldArchiveResponseDTO> response = employeeMyPageService.getSoldArchives(securityMember.getId(), page, size);

        return ApiResponse.success(SuccessStatus.GET_SOLD_ARCHIVE_SUCCESS, response);
    }

    @Operation(summary = "판매한 아카이브 총 수익 조회 (용범)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "판매한 아카이브 총 수익 조회 성공"),
    })
    @GetMapping("/archives/sold/revenue")
    public ResponseEntity<ApiResponse<String>> getSoldArchives(@AuthenticationPrincipal SecurityMember securityMember) {

        String response = employeeMyPageService.getTotalSalesRevenue(securityMember.getId());

        return ApiResponse.success(SuccessStatus.GET_TOTAL_SALES_REVENUE_SUCCESS, response);
    }
}
