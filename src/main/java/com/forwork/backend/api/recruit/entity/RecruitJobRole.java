package com.forwork.backend.api.recruit.entity;


import com.forwork.backend.api.member.entity.JobRoleEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Table(
        name = "recruit_job_role",
        indexes = {
                @Index(name = "idx_recruit_job_role", columnList = "recruit_id, job_role_id")
        }
)
@Getter
public class RecruitJobRole {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name="recruit_job_role_id")
    private Long id;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name="recruit_id")
    private Recruit recruit;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name="job_role_id")
    private JobRoleEntity jobRoleEntity;

    public RecruitJobRole(Recruit recruit, JobRoleEntity jobRoleEntity) {
        this.recruit = recruit;
        this.jobRoleEntity = jobRoleEntity;
    }
}
