package com.forwork.backend.api.recruit.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Table(
        name = "recruit_language_type",
        indexes = {
                @Index(name = "idx_recruit_language_type", columnList = "recruit_id, language_type_id")
        }
)
@Getter
public class RecruitLanguageType {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name="recruit_language_type_id")
    private Long id;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name="recruit_id")
    private Recruit recruit;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name="job_language_type")
    private LanguageTypeEntity languageTypeEntity;

    public RecruitLanguageType(Recruit recruit, LanguageTypeEntity languageTypeEntity) {
        this.recruit = recruit;
        this.languageTypeEntity = languageTypeEntity;
    }
}
