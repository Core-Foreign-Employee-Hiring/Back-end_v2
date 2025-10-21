package com.forwork.backend.api.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class JobRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="job_role_entity_id")
    private Long id;

    private String jobRole;

    public JobRoleEntity(JobRole jobRole) {
        this.jobRole = jobRole.getDbValue();
    }
}
