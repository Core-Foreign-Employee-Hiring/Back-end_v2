package com.forwork.backend.api.recruit.entity;

import com.forwork.backend.api.member.entity.JobCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

@Entity
@NoArgsConstructor
@Getter
public class JobCategoryEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="job_category_entity_id")
    private Long id;

    @Enumerated(STRING)
    private JobCategory jobCategory;

    public JobCategoryEntity(JobCategory jobCategory) {
        this.jobCategory = jobCategory;
    }
}
