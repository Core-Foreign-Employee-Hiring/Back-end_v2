package com.forwork.backend.api.plan.entity;

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
        indexes = {
                @Index(name = "idx_plan_feature_plan_version_id", columnList = "plan_version_id"),
                @Index(name = "idx_plan_feature_feature_id", columnList = "feature_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_plan_feature_plan_version_feature",
                        columnNames = {"plan_version_id", "feature_id"}
                )
        }
)
public class PlanFeature {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "plan_feature_id")
    private Long id;

    private Integer limitCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "plan_version_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PlanVersion planVersion;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "feature_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Feature feature;
}
