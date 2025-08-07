package com.forwork.backend.api.mypage.controller;

import com.forwork.backend.api.mypage.dto.response.PurchasedArchivesPreviewResponseDTO;
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
}
