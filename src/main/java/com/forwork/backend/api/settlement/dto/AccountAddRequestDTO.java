package com.forwork.backend.api.settlement.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountAddRequestDTO {

    private String accountName;
    private String accountNumber;
    private String bankName;
}
