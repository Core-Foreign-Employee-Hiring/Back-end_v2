package com.forwork.backend.api.order.dto.query;

import java.time.OffsetDateTime;

public record PassArchivePreviewIdAndPaymentApprovedAtQueryDTO(
        Long passArchiveId,
        Long paymentId,
        OffsetDateTime approvedAt
) {
}
