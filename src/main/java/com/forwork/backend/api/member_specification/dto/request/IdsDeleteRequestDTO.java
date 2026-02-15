package com.forwork.backend.api.member_specification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record IdsDeleteRequestDTO(
        @Schema(description = "ids")
        List<Long> ids
) {
}
