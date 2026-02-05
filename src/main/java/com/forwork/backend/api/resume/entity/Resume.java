package com.forwork.backend.api.resume.entity;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Table(name = "resume")
public class Resume extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "resume_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Column(nullable = false)
    private String resumeName;

    private String template;

    private String profileImageUrl;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    // 선택 항목 플래그
    private boolean includeIntroduction;
    private boolean includeEducation;
    private boolean includeCertificate;
    private boolean includeLanguage;
    private boolean includeCareer;
    private boolean includeAward;
    private boolean includeActivity;
    private boolean includeUrls;

    public void updateSelections(
            boolean includeIntroduction,
            boolean includeEducation,
            boolean includeCertificate,
            boolean includeLanguage,
            boolean includeCareer,
            boolean includeAward,
            boolean includeActivity,
            boolean includeUrls
    ) {
        this.includeIntroduction = includeIntroduction;
        this.includeEducation = includeEducation;
        this.includeCertificate = includeCertificate;
        this.includeLanguage = includeLanguage;
        this.includeCareer = includeCareer;
        this.includeAward = includeAward;
        this.includeActivity = includeActivity;
        this.includeUrls = includeUrls;
    }

    public void updateBasicInfo(String resumeName, String introduction, String profileImageUrl) {
        if (resumeName != null) {
            this.resumeName = resumeName;
        }
        this.introduction = introduction;
        this.profileImageUrl = profileImageUrl;
    }
}
