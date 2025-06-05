package com.forwork.backend.api.recruit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Table(
        name = "recruit_job_category",
        indexes = {
                @Index(name = "idx_recruit_job_category", columnList = "recruit_id, job_category_id")
        }
)
@Getter
public class RecruitJobCategory {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name="recruit_job_category_id")
    private Long id;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name="recruit_id")
    private Recruit recruit;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name="job_category_id")
    private JobCategoryEntity jobCategoryEntity;

    public RecruitJobCategory(Recruit recruit, JobCategoryEntity jobCategoryEntity) {
        this.recruit = recruit;
        this.jobCategoryEntity = jobCategoryEntity;
    }
}
