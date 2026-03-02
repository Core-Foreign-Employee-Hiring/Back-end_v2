package com.forwork.backend.api.member_specification.entity;

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
        name = "specification_evaluation",
        indexes = {
                @Index(name = "idx_member_specification_id", columnList = "member_specification_id"),
                @Index(name = "idx_score", columnList = "score")
        }
)
public class SpecificationEvaluation {
    @Id @GeneratedValue(strategy= IDENTITY)
    @Column(name="specification_evaluation_id")
    private Long id;

    private Integer experience;
    private Integer certificate;
    private Integer language;
    private Integer career;
    private Integer education;
    private Integer score;
    @Column(columnDefinition = "LONGTEXT")
    private String analysis;
    private String specName;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name = "member_specification_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberSpecification memberSpecification;
}
