package com.forwork.backend.api.pass_archive.entity;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.notification.entity.ArchiveInquiryNotification;
import com.forwork.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ArchiveInquiry extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="archive_inquiry_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String inquiry;
    @Column(columnDefinition = "TEXT")
    private String answer;

    private boolean isReadByArchiveWriter;
    private boolean isReadByInquirer;
    private boolean isAnswered;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "inquirer_id")
    private Member inquirer ;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="archive_id")
    private PassArchive archive;

    @OneToMany(mappedBy = "archiveInquiry", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<ArchiveInquiryNotification> archiveInquiryNotifications = new ArrayList<>();

    public void answer(String answer) {
        this.answer = answer;
        this.isAnswered = true;
    }

    public void readByArchiveWriter() {
        this.isReadByArchiveWriter = true;
    }

    public void readByInquirer() {
        this.isReadByInquirer = true;
    }
}
