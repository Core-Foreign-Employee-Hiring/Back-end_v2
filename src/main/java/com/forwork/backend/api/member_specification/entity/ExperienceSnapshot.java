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
        indexes = @Index(name = "idx_experience_snapshot_specification_evaluation_id", columnList = "specification_evaluation_id")
)
public class ExperienceSnapshot extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "experience_snapshot_id")
    private Long id;

    private String experience;
    private Double beforeImprovementRate;
    private Double afterImprovementRate;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String startDate;
    private String endDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "specification_evaluation_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private SpecificationEvaluation specificationEvaluation;
}
