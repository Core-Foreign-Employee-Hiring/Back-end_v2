package com.forwork.backend.api.member.repository;

import com.forwork.backend.api.member.entity.MemberAward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberAwardRepository extends JpaRepository<MemberAward, Long> {
    @Query("select a from MemberAward a" +
            " where a.memberSpecification.id=:memberSpecificationId")
    List<MemberAward> findByMemberSpecificationId(@Param("memberSpecificationId") Long memberSpecificationId);
}
