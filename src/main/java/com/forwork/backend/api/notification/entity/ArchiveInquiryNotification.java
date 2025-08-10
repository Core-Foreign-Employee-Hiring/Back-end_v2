package com.forwork.backend.api.notification.entity;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.notification.enums.ArchiveInquiryNotificationType;
import com.forwork.backend.api.pass_archive.entity.ArchiveInquiry;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor
@DiscriminatorValue("archive_inquiry")
public class ArchiveInquiryNotification extends Notification{

    @Enumerated(EnumType.STRING)
    private ArchiveInquiryNotificationType notificationType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="archive_inquiry_id")
    private ArchiveInquiry archiveInquiry;

    public ArchiveInquiryNotification(ArchiveInquiryNotificationType notificationType, ArchiveInquiry archiveInquiry, Member receiver) {
        super(receiver);
        this.notificationType = notificationType;
        this.archiveInquiry = archiveInquiry;
    }
}
