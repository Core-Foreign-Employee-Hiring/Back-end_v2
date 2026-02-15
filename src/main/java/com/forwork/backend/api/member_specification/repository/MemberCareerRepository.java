package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.MemberCareer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberCareerRepository extends JpaRepository<MemberCareer, Long> {
    @Query("select c from MemberCareer c" +
            " where c.memberSpecification.id=:memberSpecificationId")
    List<MemberCareer> findByMemberSpecificationId(@Param("memberSpecificationId") Long memberSpecificationId);

    @Query("select a from MemberCareer a" +
            " join fetch a.memberSpecification ms" +
            " join fetch ms.member" +
            " where a.id=:careerId")
    Optional<MemberCareer> findByIdWithMember(@Param("careerId") Long careerId);

    @Modifying
    @Transactional
    @Query("delete from MemberCareer mc where mc.memberSpecification.id=:memberSpecificationId")
    void deleteByMemberSpecificationId(@Param("memberSpecificationId") Long memberSpecificationId);
}