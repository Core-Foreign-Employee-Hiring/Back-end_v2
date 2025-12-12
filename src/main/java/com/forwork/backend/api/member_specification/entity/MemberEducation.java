package com.forwork.backend.api.member_specification.entity;

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
@Table(
        name="member_education",
        indexes = @Index(name="idx_member_specification_id", columnList = "member_specification_id")
)
public class MemberEducation extends BaseTimeEntity {
    @Id @GeneratedValue(strategy= IDENTITY)
    @Column(name="member_education_id")
    private Long id;

    private String schoolName;       // 학교 이름
    private Double earnedScore;  // 내 학점
    private Double maxScore;     // 총점

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name = "member_specification_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberSpecification memberSpecification;
}
