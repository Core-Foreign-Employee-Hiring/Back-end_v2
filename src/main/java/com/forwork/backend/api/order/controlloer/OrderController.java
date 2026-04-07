package com.forwork.backend.api.order.controlloer;

import com.forwork.backend.api.order.dto.request.OrderRequest;
import com.forwork.backend.api.order.dto.response.OrderResponse;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "주문 관련 API 입니다.")
@RestController
@RequestMapping("/api/v3/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    /*
     * c
     * */

    @Operation(summary = "주문 생성 (용범)", description =
            "결제 전 주문 생성 api<p>" +
                    "요청: OrderRequest" +
                    "<p>" +
                    "응답: OrderResponse<p>" +
                    "주문 생성 이유: <A href = \"https://docs.tosspayments.com/guides/v2/get-started/payment-flow#결제-요청-전에-결제할-데이터-저장하기\" target=\"_blank\"> 이동 하기 </A>"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "주문 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미 등록된 주문번호입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "합격 아카이브를 찾을 수 없습니다."),
    })
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@AuthenticationPrincipal SecurityMember securityMember,
                                                                  @Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.create(securityMember.getId(), request);

        return ApiResponse.success(SuccessStatus.ORDER_CREATE_SUCCESS, response);
    }
}
