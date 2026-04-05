package com.forwork.backend.api.plan.entity;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Table(
        indexes = {
                @Index(name = "idx_plan_usage_log_member_id", columnList = "member_id"),
                @Index(name = "idx_plan_usage_log_feature_id", columnList = "feature_id"),
                @Index(name = "idx_plan_usage_log_subscription_id", columnList = "subscription_id"),
                @Index(name = "idx_plan_usage_log_member_id_used_date", columnList = "member_id, used_date")
        }
)
public class PlanUsageLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "plan_usage_log_id")
    private Long id;

    private OffsetDateTime usedAt;
    private LocalDate usedDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "feature_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Feature feature;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "subscription_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Subscription subscription;
}
