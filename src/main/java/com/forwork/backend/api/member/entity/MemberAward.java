package com.forwork.backend.api.member.entity;

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
        name="member_award",
        indexes = @Index(name="idx_member_specification_id", columnList = "member_specification_id")
)
public class MemberAward {
    @Id @GeneratedValue(strategy= IDENTITY)
    @Column(name="member_career_id")
    private Long id;

    private String awardName;
    private String host;
    private Integer acquiredYear;
    private Integer acquiredMonth;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String documentUrl;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name = "member_specification_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberSpecification memberSpecification;
}
