package com.forwork.backend.api.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrderRequestDTO(
        @Schema(description = "merchantOrderId")
        String merchantOrderId ,
        @Schema(description = "결제 금액")
        String amount,
        @Schema(description="아카이브 ids. 혹시 몰라 List 로 받음 1개면 그냥 1개 넘겨주면 됨.")
        @Size(min = 1, max = 1)
        List<Long> passArchiveIds
) {

}
