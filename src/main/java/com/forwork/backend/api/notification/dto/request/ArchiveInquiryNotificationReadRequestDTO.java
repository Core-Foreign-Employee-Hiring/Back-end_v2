package com.forwork.backend.api.notification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ArchiveInquiryNotificationReadRequestDTO(
        @Schema(description = "읽은 notificationIds")
        List<Long> notificationIds
) {
}
