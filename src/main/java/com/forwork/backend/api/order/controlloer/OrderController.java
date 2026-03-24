package com.forwork.backend.api.order.controlloer;

import com.forwork.backend.api.order.dto.request.CashReceiptIssueRequest;
import com.forwork.backend.api.order.dto.request.OrderRequestDTO;
import com.forwork.backend.api.order.dto.response.OrderResponseDTO;
import com.forwork.backend.api.order.service.OrderService;
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

@Tag(name = "Order", description = "주문 관련 API 입니다.")
@RestController
@RequestMapping("/api/v2/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;


    /*
     * c
     * */

    @Operation(summary = "주문 생성 (용범)", description =
            "결제 전 주문 생성 api<p>" +
                    "입력: OrderRequestDTO" +
                    "<p>" +
                    "출력: merchantOrderId<p>" +
                    "주문 생성 이유: <A href = \"https://docs.tosspayments.com/guides/v2/get-started/payment-flow#결제-요청-전에-결제할-데이터-저장하기\" target=\"_blank\"> 이동 하기 </A>"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "주문 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미 등록된 주문번호입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "합격 아카이브를 찾을 수 없습니다."),
    })
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO,
                                                                     @AuthenticationPrincipal SecurityMember securityMember) {
        OrderResponseDTO response = orderService.createOrder(securityMember.getId(), orderRequestDTO);

        return ApiResponse.success(SuccessStatus.ORDER_CREATE_SUCCESS, response);
    }

    @Operation(summary = "현금영수증 발행  (용범)", description =
            "입력: CashReceiptIssueRequest" +
                    "<p>" +
                    "참고: <a href=\"https://docs.tosspayments.com/reference#현금영수증-발급-요청\" target=\"_blank\">현금영수증 발급 요청</a>" +
                    "<p>" +
                    "예외: <a href=\"https://docs.tosspayments.com/reference/error-codes#%ED%98%84%EA%B8%88%EC%98%81%EC%88%98%EC%A6%9D-%EB%B0%9C%EA%B8%89-%EC%9A%94%EC%B2%AD\" target=\"_blank\">이동하기</a>"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "현금영수증 발급 성공"),
    })
    @PostMapping("/{merchant-order-id}/cash-receipt")
    public ResponseEntity<ApiResponse<Void>> issueCashReceipt(@Valid @RequestBody CashReceiptIssueRequest request,
                                                              @AuthenticationPrincipal SecurityMember securityMember,
                                                              @PathVariable("merchant-order-id") String merchantOrderId) {

        orderService.issueCashReceipt(securityMember.getId(), request, merchantOrderId);

        return ApiResponse.success(SuccessStatus.CASH_RECEIPT_ISSUE_SUCCESS);
    }


    /*
     * r
     * */

    @Operation(summary = "주문 조회 (용범)", description = "출력: OrderResponseDTO"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "주문 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "해당 주문에 접근할 권한이 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 주문을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "합격 아카이브를 찾을 수 없습니다."),
    })
    @GetMapping("/{merchant-order-id}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrder(@AuthenticationPrincipal SecurityMember securityMember,
                                                                  @PathVariable("merchant-order-id") String merchantOrderId) {
        OrderResponseDTO response = orderService.getOrder(securityMember.getId(), merchantOrderId);

        return ApiResponse.success(SuccessStatus.ORDER_GET_SUCCESS, response);
    }
}
