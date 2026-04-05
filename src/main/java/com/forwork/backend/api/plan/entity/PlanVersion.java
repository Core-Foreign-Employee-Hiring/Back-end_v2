package com.forwork.backend.api.plan.entity;

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
        indexes = @Index(name = "idx_plan_version_plan_id", columnList = "plan_id")
)
public class PlanVersion extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "plan_version_id")
    private Long id;

    private Long price;
    private boolean active;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "plan_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Plan plan;
}
