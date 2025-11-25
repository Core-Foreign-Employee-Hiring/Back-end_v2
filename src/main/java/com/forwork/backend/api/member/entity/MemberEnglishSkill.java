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
        name="member_english_skill",
        indexes = @Index(name="idx_member_language_skill_id", columnList = "member_language_skill_id")
)
public class MemberEnglishSkill {
    @Id @GeneratedValue(strategy= IDENTITY)
    @Column(name="member_english_skill_id")
    private Long id;

    private String type;
    private String score;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name = "member_language_skill_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberLanguageSkill memberLanguageSkill;
}
