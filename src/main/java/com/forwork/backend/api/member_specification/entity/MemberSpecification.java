package com.forwork.backend.api.member_specification.entity;

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

public class MemberSpecification extends BaseTimeEntity {
    @Id @GeneratedValue(strategy= IDENTITY)
    @Column(name="member_specification_id")
    private Long id;


    @OneToOne(fetch = LAZY)
    @JoinColumn(name="member_id",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT)
    )
    private Member member;
}
