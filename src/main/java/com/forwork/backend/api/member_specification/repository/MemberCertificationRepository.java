package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.MemberCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberCertificationRepository extends JpaRepository<MemberCertification, Long> {

    @Query("select c from MemberCertification c" +
            " where c.memberSpecification.id=:memberSpecificationId")
    List<MemberCertification> findByMemberSpecification(@Param("memberSpecificationId") Long memberSpecificationId);

    @Modifying @Transactional
    @Query("delete from MemberCertification mc where mc.memberSpecification.id=:memberSpecificationId")
    void deleteByMemberSpecificationId(@Param("memberSpecificationId") Long memberSpecificationId);
}
