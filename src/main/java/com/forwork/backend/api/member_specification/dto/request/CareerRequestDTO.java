package com.forwork.backend.api.member_specification.dto.request;

import com.forwork.backend.api.recruit.enums.ContractType;
import com.forwork.backend.common.validation.ValidContractType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record CareerRequestDTO(
        @Schema(description = "경력사항")
        @Valid
        List<Career> careers

) {
    public record Career(

            @Schema(description = "회사명")
            @NotBlank
            String companyName,

            @Schema(description = "포지션")
            @NotBlank
            String position,

            @Schema(
                    description = "근무 시작일",
                    format = "yyyy-MM"
            )
            @NotBlank
            @Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])", message = "yyyy-MM 형식이어야 합니다.")
            String startDate,

            @Schema(
                    description = "근무 종료일(null -> 재직 중)",
                    format = "yyyy-MM"
            )
            @Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])", message = "yyyy-MM 형식이어야 합니다.")
            String endDate,

            @Schema(description = "계약형태")
            @ValidContractType(anyOf = {ContractType.CONTRACT, ContractType.REGULAR, ContractType.INTERN})
            ContractType contractType,

            @Schema(description = "어필 경험")
            String highlight

    ) {
    }
}
