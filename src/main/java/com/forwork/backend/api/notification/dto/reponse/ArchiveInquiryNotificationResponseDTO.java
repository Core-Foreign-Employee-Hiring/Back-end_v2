package com.forwork.backend.api.notification.dto.reponse;

import com.forwork.backend.api.notification.entity.ArchiveInquiryNotification;
import com.forwork.backend.api.notification.enums.ArchiveInquiryNotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

public record ArchiveInquiryNotificationResponseDTO(
        @Schema(description = "알림 id")
        Long archiveInquiryNotificationId,
        @Schema(description = "문의 id")
        Long archiveInquiryId,
        @Schema(description = "알람 타입. INQUIRY: 답변 남기기 해야 함, ANSWER: 답변 보기 해야 함.")
        ArchiveInquiryNotificationType type,
        @Schema(description = "사람 이름")
        String name,
        @Schema(description = "아카이브 제목")
        String title,
        @Schema(description = "읽음 유무")
        boolean read
) {
    public static ArchiveInquiryNotificationResponseDTO of(ArchiveInquiryNotification noti){
        ArchiveInquiryNotificationType type = noti.getNotificationType();
        String name = null;
        String title = noti.getArchiveInquiry().getArchive().getTitle();

        if (type.equals(ArchiveInquiryNotificationType.INQUIRY)) {
            name = noti.getArchiveInquiry().getInquirer().getName();
        }
        else if(type.equals(ArchiveInquiryNotificationType.ANSWER)){
            name = noti.getArchiveInquiry().getArchive().getMember().getName();
        }

        name = name.charAt(0) + "**";

        return new ArchiveInquiryNotificationResponseDTO(
                noti.getId(),
                noti.getArchiveInquiry().getId(),
                type,
                name,
                title,
                noti.isRead()
        );
    }
}
