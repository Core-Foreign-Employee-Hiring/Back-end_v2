package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.MemberCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberCertificationRepository extends JpaRepository<MemberCertification, Long> {

    @Query("select c from MemberCertification c" +
            " where c.memberSpecification.id=:memberSpecificationId")
    List<MemberCertification> findByMemberSpecification(@Param("memberSpecificationId") Long memberSpecificationId);

    @Query("select a from MemberCertification a" +
            " join fetch a.memberSpecification ms" +
            " join fetch ms.member" +
            " where a.id=:id")
    Optional<MemberCertification> findByIdWithMember(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("delete from MemberCertification mc where mc.memberSpecification.id=:memberSpecificationId")
    void deleteByMemberSpecificationId(@Param("memberSpecificationId") Long memberSpecificationId);
}
