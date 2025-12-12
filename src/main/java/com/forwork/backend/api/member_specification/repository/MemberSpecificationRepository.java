package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.MemberSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberSpecificationRepository extends JpaRepository<MemberSpecification, Long> {

    @Query("select s from MemberSpecification s" +
            " where s.member.id=:memberId")
    Optional<MemberSpecification> findByMemberIdId(@Param("memberId") Long memberId);
}
