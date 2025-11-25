package com.forwork.backend.api.member.repository;

import com.forwork.backend.api.member.entity.MemberExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberExperienceRepository extends JpaRepository<MemberExperience, Long> {

    @Query("select e from MemberExperience e" +
            " where e.memberSpecification.id=:memberSpecificationId")
    List<MemberExperience> findByMemberSpecificationId(@Param("memberSpecificationId") Long memberSpecificationId);
}
