package com.forwork.backend.api.member.entity;

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
        name="member_lanuage_skill",
        indexes = @Index(name="idx_member_specification_id", columnList = "member_specification_id")
)
public class MemberLanguageSkill extends BaseTimeEntity {
    @Id @GeneratedValue(strategy= IDENTITY)
    @Column(name="member_language_skill_id")
    private Long id;

    private Integer klptScore;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name = "member_specification_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberSpecification memberSpecification;
}
