package com.forwork.backend.api.settlement.controller;

import com.forwork.backend.api.settlement.dto.AccountAddRequestDTO;
import com.forwork.backend.api.settlement.dto.AccountResponseDTO;
import com.forwork.backend.api.settlement.dto.WithdrawerInfoResponseDTO;
import com.forwork.backend.api.settlement.service.AccountService;
import com.forwork.backend.common.config.security.SecurityMember;
import com.forwork.backend.common.response.ApiResponse;
import com.forwork.backend.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Settlement", description = "정산 관련 API 입니다.")
@RestController
@RequestMapping("/api/v2/settlement")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Operation(
            summary = "계좌번호 등록 API (태근)",
            description = "계좌번호 등록을 진행합니다. <br>"
                    + "<p>"
                    + "호출 필드 정보) <br>"
                    + "accountName : 예금주 <br>"
                    + "accountNumber : 계좌번호 <br>"
                    + "bankName : 은행 <br>"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "계좌정보 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @PostMapping("/account")
    public ResponseEntity<ApiResponse<Void>> addAccount(@RequestBody AccountAddRequestDTO accountAddRequestDTO, @AuthenticationPrincipal SecurityMember securityMember) {

        accountService.addAccount(accountAddRequestDTO, securityMember.getId());
        return ApiResponse.success_only(SuccessStatus.CREATE_ACCOUNT_SUCCESS);
    }

    @Operation(
            summary = "계좌번호 조회 API (태근)",
            description = "계좌번호 조회를 진행합니다. <br>"
                    + "<p>"
                    + "응답 필드 정보) <br>"
                    + "accountName : 예금주 <br>"
                    + "accountNumber : 계좌번호 <br>"
                    + "bankName : 은행 <br>"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "계좌정보 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "등록된 계좌정보를 찾을 수 없습니다.")
    })
    @GetMapping("/account")
    public ResponseEntity<ApiResponse<AccountResponseDTO>> viewAccount(@AuthenticationPrincipal SecurityMember securityMember) {

        AccountResponseDTO accountResponseDTO = accountService.getAccount(securityMember.getId());
        return ApiResponse.success(SuccessStatus.SEND_ACCOUNT_INFO_SUCCESS, accountResponseDTO);
    }

    @Operation(
            summary = "계좌번호 수정 API (태근)",
            description = "계좌번호 수정을 진행합니다. <br>"
                    + "<p>"
                    + "호출 필드 정보) <br>"
                    + "accountName : 예금주 <br>"
                    + "accountNumber : 계좌번호 <br>"
                    + "bankName : 은행 <br>"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "계좌정보 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "등록된 계좌정보를 찾을 수 없습니다.")
    })
    @PutMapping("/account")
    public ResponseEntity<ApiResponse<Void>> modifyAccount(@RequestBody AccountAddRequestDTO accountAddRequestDTO, @AuthenticationPrincipal SecurityMember securityMember) {

        accountService.modifyAccount(accountAddRequestDTO, securityMember.getId());
        return ApiResponse.success_only(SuccessStatus.MODIFY_ACCOUNT_SUCCESS);
    }

    @Operation(
            summary = "인출자 정보 조회 API (용범)", description = "출력: WithdrawerInfoResponseDTO <br>"

    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "인출자 정보 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "등록된 계좌정보를 찾을 수 없습니다."),
    })
    @GetMapping("/withdrawer")
    public ResponseEntity<ApiResponse<WithdrawerInfoResponseDTO>> getWithdrawerInfo(@AuthenticationPrincipal SecurityMember securityMember) {

        WithdrawerInfoResponseDTO response = accountService.getWithdrawerInfo(securityMember.getId());
        return ApiResponse.success(SuccessStatus.SEND_WITHDRAWER_INFO_SUCCESS, response);
    }
}
