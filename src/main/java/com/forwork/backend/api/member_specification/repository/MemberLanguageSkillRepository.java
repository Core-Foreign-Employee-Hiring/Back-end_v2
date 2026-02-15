package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.MemberLanguageSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberLanguageSkillRepository extends JpaRepository<MemberLanguageSkill, Long> {

    @Query("select l from MemberLanguageSkill l" +
            " where l.memberSpecification.id=:memberSpecificationId")
    List<MemberLanguageSkill> findByMemberSpecificationId(@Param("memberSpecificationId") Long memberSpecificationId);

    @Query("select a from MemberLanguageSkill a" +
            " join fetch a.memberSpecification ms" +
            " join fetch ms.member" +
            " where a.id=:id")
    Optional<MemberLanguageSkill> findByIdWithMember(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("delete from MemberLanguageSkill mls where mls.memberSpecification.id=:memberSpecificationId")
    void deleteByMemberSpecificationId(@Param("memberSpecificationId") Long memberSpecificationId);
}
