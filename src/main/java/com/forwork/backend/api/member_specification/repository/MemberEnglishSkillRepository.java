package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.MemberEnglishSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberEnglishSkillRepository extends JpaRepository<MemberEnglishSkill, Long> {

    @Query("select e from MemberEnglishSkill e" +
            " where e.memberLanguageSkill.id=:memberLanguageSkillId")
    List<MemberEnglishSkill> findByMemberLanguageSkillId(@Param("memberLanguageSkillId")Long memberLanguageSkillId);
}
