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
        indexes = @Index(name = "idx_certification_snapshot_specification_evaluation_id", columnList = "specification_evaluation_id")
)
public class CertificationSnapshot extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "certification_snapshot_id")
    private Long id;

    private String certificationName;
    private String acquiredDate;
    private String documentUrl;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "specification_evaluation_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private SpecificationEvaluation specificationEvaluation;
}
