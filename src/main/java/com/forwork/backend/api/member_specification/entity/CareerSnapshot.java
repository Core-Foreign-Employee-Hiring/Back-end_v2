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
        indexes = @Index(name = "idx_career_snapshot_specification_evaluation_id", columnList = "specification_evaluation_id")
)
public class CareerSnapshot extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "career_snapshot_id")
    private Long id;

    private String companyName;
    private String position;
    private String startDate;
    private String endDate;
    private String contractType;
    @Column(columnDefinition = "LONGTEXT")
    private String highlight;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "specification_evaluation_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private SpecificationEvaluation specificationEvaluation;
}
