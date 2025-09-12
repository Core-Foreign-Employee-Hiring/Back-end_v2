package com.forwork.backend.api.settlement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountAddRequestDTO {

    private String accountName;
    private String accountNumber;
    private String bankName;
}
