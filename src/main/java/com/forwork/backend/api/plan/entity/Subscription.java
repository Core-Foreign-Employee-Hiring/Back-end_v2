package com.forwork.backend.api.plan.entity;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Table(
        indexes = {
                @Index(name = "idx_subscription_member_id", columnList = "member_id"),
                @Index(name = "idx_subscription_plan_version_id", columnList = "plan_version_id")
        }
)
public class Subscription extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "subscription_id")
    private Long id;

    private String subscriptionStatus;

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "plan_version_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PlanVersion planVersion;
}
