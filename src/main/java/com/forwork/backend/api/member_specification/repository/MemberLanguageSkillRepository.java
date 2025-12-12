package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.MemberLanguageSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberLanguageSkillRepository extends JpaRepository<MemberLanguageSkill, Long> {

    @Query("select l from MemberLanguageSkill l" +
            " where l.memberSpecification.id=:memberSpecificationId")
    Optional<MemberLanguageSkill> findByMemberSpecificationId(@Param("memberSpecificationId")Long memberSpecificationId);
}
