package com.forwork.backend.api.pay.controller;

import com.forwork.backend.api.order.dto.response.OrderResponseDTO;
import com.forwork.backend.api.order.enums.ItemType;
import com.forwork.backend.api.pay.dto.request.PaymentConfirmRequestDTO;
import com.forwork.backend.api.pay.dto.response.ArchivePaymentHistoryResponse;
import com.forwork.backend.api.pay.dto.response.PaymentHistoryResponse;
import com.forwork.backend.api.pay.service.PaymentService;
import com.forwork.backend.api.pay.service.PaymentTestService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payment", description =
        "결제 관련 API 입니다." +
                "<p>" +
                "tosspayments 결제 흐름: <A href = \"https://docs.tosspayments.com/guides/v2/get-started/payment-flow#결제-흐름-이해하기\" target=\"_blank\"> 이동 하기 </A>"
)
@RestController
@RequestMapping("/api/v2/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentTestService paymentTestService;


    /*
     * create
     * */

    @Operation(
            summary = "결제 승인 요청 API (용범)",
            description =
                    "토스페이먼트에 결제 승인 요청을 수행합니다.<br>" +
                            "입력: PaymentConfirmRequestDTO" +
                            "<p>" +
                            "tosspayments 결제 승인: <A href = \"https://docs.tosspayments.com/reference#결제-승인\" target=\"_blank\"> 이동 하기 </A>"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "결제 승인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "4XX", description =
                    "결제 승인 실패<b>" +
                            "<p>" +
                            "tosspayments: <A href = \"https://docs.tosspayments.com/reference/error-codes#결제-승인\" target=\"_blank\"> 이동 하기 </A>"
            ),
    })
    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<Void>> confirmPayment(@Valid @RequestBody PaymentConfirmRequestDTO paymentConfirmRequestDTO,
                                                            @AuthenticationPrincipal SecurityMember securityMember) {
        paymentService.requestConfirm(paymentConfirmRequestDTO);

        return ApiResponse.success_only(SuccessStatus.SEND_PAY_SUCCESS);
    }


    /*
     * read
     * */


    @Operation(
            summary = "아카이브 결제 내역 조회 API (용범)",
            description = "출력: ArchivePaymentHistoryResponse"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "결제 내역 조회 성공"),
    })
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<PageResponseDTO<ArchivePaymentHistoryResponse>>> getArchivePaymentHistory(
            @AuthenticationPrincipal SecurityMember securityMember,

            @Parameter(description = "페이지 번호 (0부터 시작)", in = ParameterIn.QUERY)
            @RequestParam(value = "page", defaultValue = "0") Integer page,

            @Parameter(description = "페이지 크기", in = ParameterIn.QUERY)
            @RequestParam(value = "size", defaultValue = "10") Integer size) {


        PageResponseDTO<ArchivePaymentHistoryResponse> response = paymentService.getArchivePaymentHistory(securityMember.getId(), page, size);

        return ApiResponse.success(SuccessStatus.PAYMENT_HISTORY_SUCCESS, response);
    }


    @Operation(
            summary = "결제 내역 조회 API (용범)",
            description = ""
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "결제 내역 조회 성공"),
    })
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<PaymentHistoryResponse>>> getPaymentHistory(
            @AuthenticationPrincipal SecurityMember securityMember,

            @Parameter(description = "itemType", in = ParameterIn.QUERY)
            @RequestParam(value = "itemType") ItemType itemType,

            @Parameter(description = "페이지 번호 (0부터 시작)", in = ParameterIn.QUERY)
            @RequestParam(value = "page", defaultValue = "0") Integer page,

            @Parameter(description = "페이지 크기", in = ParameterIn.QUERY)
            @RequestParam(value = "size", defaultValue = "10") Integer size) {


        PageResponseDTO<PaymentHistoryResponse> response = paymentService.getPaymentHistory(securityMember.getId(), itemType, page, size);

        return ApiResponse.success(SuccessStatus.PAYMENT_HISTORY_SUCCESS, response);
    }

    /*
     * 테스트
     * */

    @Operation(
            summary = "테스트: 결제 없이 아카이브 구매 API (용범)", description = "")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "결제 승인 성공"),
    })
    @PostMapping("/test/confirm")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> test_confirmPayment(@AuthenticationPrincipal SecurityMember securityMember,
                                                                             @RequestParam("archiveId") Long archiveId) {
        OrderResponseDTO response = paymentTestService.requestConfirm(securityMember.getId(), archiveId);

        return ApiResponse.success(SuccessStatus.SEND_PAY_SUCCESS, response);
    }
}
