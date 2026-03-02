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
        indexes = @Index(name = "idx_education_snapshot_specification_evaluation_id", columnList = "specification_evaluation_id")
)
public class EducationSnapshot extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "education_snapshot_id")
    private Long id;

    private String schoolName;        // 학교 이름
    private String admissionDate;     // 입학
    private String graduationDate;    // 졸업
    private Double earnedScore;       // 내 학점
    private Double maxScore;          // 총점

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "specification_evaluation_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private SpecificationEvaluation specificationEvaluation;
}
