package com.forwork.backend.api.member.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class JobCategoryEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="job_category_entity_id")
    private Long id;

    private String jobCategory;

    public JobCategoryEntity(JobCategory jobCategory) {
        this.jobCategory = jobCategory.getDbValue();
    }
}
