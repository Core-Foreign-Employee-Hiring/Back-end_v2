package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.MemberExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberExperienceRepository extends JpaRepository<MemberExperience, Long> {

    @Query("select e from MemberExperience e" +
            " where e.memberSpecification.id=:memberSpecificationId")
    List<MemberExperience> findByMemberSpecificationId(@Param("memberSpecificationId") Long memberSpecificationId);

    @Query("select a from MemberExperience a" +
            " join fetch a.memberSpecification ms" +
            " join fetch ms.member" +
            " where a.id=:id")
    Optional<MemberExperience> findByIdWithMember(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("delete from MemberExperience me where me.memberSpecification.id=:memberSpecificationId")
    void deleteByMemberSpecificationId(@Param("memberSpecificationId") Long memberSpecificationId);
}
