package com.forwork.backend.api.member.repository;

import com.forwork.backend.api.member.entity.MemberCareer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberCareerRepository extends JpaRepository<MemberCareer, Long> {
    @Query("select c from MemberCareer c" +
            " where c.memberSpecification.id=:memberSpecificationId")
    List<MemberCareer> findByMemberSpecificationId(@Param("memberSpecificationId") Long memberSpecificationId);
}
