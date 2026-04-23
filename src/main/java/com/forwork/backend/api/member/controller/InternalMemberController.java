package com.forwork.backend.api.member.controller;

import com.forwork.backend.api.plan.dto.response.PlanResponse;
import com.forwork.backend.api.plan.service.SubscriptionService;
import com.forwork.backend.common.response.ApiResponse;
import com.forwork.backend.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Internal Member", description = "Internal Member 관련 API 입니다.")
@RestController
@RequestMapping("/internal/v1/members")
@RequiredArgsConstructor
public class InternalMemberController {
    private final SubscriptionService subscriptionService;

    @Operation(
            summary = "사용자 plan 정보 조회 API (용범)",
            description = "응답: PlanResponse"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "플랜 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "플랜 정보를 찾을 수 없습니다.")
    })
    @GetMapping("/{memberId}/plan")
    public ResponseEntity<ApiResponse<PlanResponse>> getPlan(@PathVariable Long memberId) {
        PlanResponse response = subscriptionService.getPlan(memberId);

        return ApiResponse.success(SuccessStatus.PLAN_GET_SUCCESS, response);
    }
}
