package com.forwork.backend.api.member.repository;

import com.forwork.backend.api.member.entity.MemberCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberCertificationRepository extends JpaRepository<MemberCertification, Long> {

    @Query("select c from MemberCertification c" +
            " where c.memberSpecification.id=:memberSpecificationId")
    List<MemberCertification> findByMemberSpecification(@Param("memberSpecificationId") Long memberSpecificationId);
}
