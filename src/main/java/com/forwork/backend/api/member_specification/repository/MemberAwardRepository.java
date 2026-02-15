package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.MemberAward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberAwardRepository extends JpaRepository<MemberAward, Long> {
    @Query("select a from MemberAward a" +
            " where a.memberSpecification.id=:memberSpecificationId")
    List<MemberAward> findByMemberSpecificationId(@Param("memberSpecificationId") Long memberSpecificationId);


    @Query("select a from MemberAward a" +
            " join fetch a.memberSpecification ms" +
            " join fetch ms.member" +
            " where a.id=:awardId")
    Optional<MemberAward> findByIdWithMember(@Param("awardId") Long awardId);

    @Modifying
    @Transactional
    @Query("delete from MemberAward ma where ma.memberSpecification.id=:memberSpecificationId")
    void deleteByMemberSpecificationId(@Param("memberSpecificationId") Long memberSpecificationId);
}
