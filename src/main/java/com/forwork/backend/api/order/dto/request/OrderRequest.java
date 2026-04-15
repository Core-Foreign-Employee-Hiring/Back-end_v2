package com.forwork.backend.api.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrderRequest(
        @Schema(description = "itemIds")
        @Size(min = 1)
        List<Long> itemIds
) {
}
