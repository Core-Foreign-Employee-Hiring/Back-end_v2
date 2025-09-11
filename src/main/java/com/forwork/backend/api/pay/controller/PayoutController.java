package com.forwork.backend.api.pay.controller;

import com.forwork.backend.api.pay.dto.request.PayoutRequestDTO;
import com.forwork.backend.api.pay.dto.response.TestPayoutResponseDTO;
import com.forwork.backend.api.pay.enums.PayoutStatus;
import com.forwork.backend.api.pay.service.PayoutService;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payout", description = "인출 관련 API 입니다.")
@RestController
@RequestMapping("/api/v2/payout")
@RequiredArgsConstructor
public class PayoutController {
    private final PayoutService payoutService;



    /*
    * c
    * */

    @Operation(
            summary = "인출 요청 API (용범)", description = "입력: PayoutRequestDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "인출 요청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미 인출 요청한 결제 내역입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "인출 요청자와 판매자가 일치하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없습니다."),
    })
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> requestPayout(@AuthenticationPrincipal SecurityMember securityMember,
                                                           @RequestBody PayoutRequestDTO payoutRequestDTO) {
        payoutService.requestPayout(securityMember.getId(), payoutRequestDTO.paymentIds());

        return ApiResponse.success_only(SuccessStatus.REQUEST_PAYOUT_SUCCESS);
    }


    /*
    * 테스트
    * */

    @Operation(
            summary = "테스트:: 인출 조회 API", description = "출력: TestPayoutResponseDTO"
    )
    @ApiResponses({})
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<TestPayoutResponseDTO>>> getPayouts(
            @AuthenticationPrincipal SecurityMember securityMember,

            @Parameter(description = "판매자 id", in = ParameterIn.QUERY)
            @RequestParam(value = "sellerId", required = false) Long sellerId,

            @Parameter(description = "인출 상태", in = ParameterIn.QUERY)
            @RequestParam(value = "payoutStatus", required = false) PayoutStatus payoutStatus,


            @Parameter(description = "페이지 번호 (0부터 시작)", in = ParameterIn.QUERY)
            @RequestParam(value = "page", defaultValue = "0") Integer page,

            @Parameter(description = "페이지 크기", in = ParameterIn.QUERY)
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        PageResponseDTO<TestPayoutResponseDTO> response = payoutService.getPayouts(sellerId, payoutStatus, page, size);

        return ApiResponse.success(SuccessStatus.REQUEST_PAYOUT_SUCCESS, response);
    }

}
