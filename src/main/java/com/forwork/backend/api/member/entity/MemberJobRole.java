package com.forwork.backend.api.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
public class MemberJobRole {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name="member_job_role_id")
    private Long id;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name="job_role_id")
    private JobRoleEntity jobRoleEntity;

    public MemberJobRole(Member member, JobRoleEntity jobRoleEntity) {
        this.member = member;
        this.jobRoleEntity = jobRoleEntity;
    }
}
