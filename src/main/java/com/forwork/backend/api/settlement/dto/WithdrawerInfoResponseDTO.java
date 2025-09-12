package com.forwork.backend.api.settlement.dto;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.settlement.entity.Account;
import io.swagger.v3.oas.annotations.media.Schema;

public record WithdrawerInfoResponseDTO(
        @Schema(description = "예금주")
        String accountName,
        @Schema(description = "계좌번호")
        String accountNumber,
        @Schema(description = "은행 이름")
        String bankName,
        @Schema(description = "이메일")
        String email
) {

        public static WithdrawerInfoResponseDTO of(Member member, Account account) {
                return new WithdrawerInfoResponseDTO(
                        account.getAccountName(),
                        account.getAccountNumber(),
                        account.getBankName(),
                        member.getEmail()
                );
        }
}
