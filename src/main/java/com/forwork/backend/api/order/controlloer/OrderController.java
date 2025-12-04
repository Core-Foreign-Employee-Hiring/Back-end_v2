package com.forwork.backend.api.order.controlloer;

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
            "결제 전 주문 생성 api<b>" +
            "입력: OrderRequestDTO" +
            "<p>" +
            "주문 생성 이유: <A href = \"https://docs.tosspayments.com/guides/v2/get-started/payment-flow#결제-요청-전에-결제할-데이터-저장하기\" target=\"_blank\"> 이동 하기 </A>"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "주문 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미 등록된 주문번호입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "합격 아카이브를 찾을 수 없습니다."),
    })
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO,
                                                         @AuthenticationPrincipal SecurityMember securityMember) {
        orderService.createOrder(securityMember.getId(), orderRequestDTO);

        return ApiResponse.success_only(SuccessStatus.ORDER_CREATE_SUCCESS);
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
