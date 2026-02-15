package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.MemberEducation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberEducationRepository extends JpaRepository<MemberEducation, Long> {

    @Query("select e from MemberEducation e" +
            " where e.memberSpecification.id=:memberSpecificationId" +
            " order by e.id asc" +
            " limit 1")
    Optional<MemberEducation> findByMemberSpecificationId(@Param("memberSpecificationId") Long memberSpecificationId);

    @Query("select a from MemberEducation a" +
            " join fetch a.memberSpecification ms" +
            " join fetch ms.member" +
            " where a.id=:id")
    Optional<MemberEducation> findByIdWithMember(@Param("id") Long id);


    List<MemberEducation> findAllByMemberSpecificationId(@Param("memberSpecificationId") Long memberSpecificationId);
}
