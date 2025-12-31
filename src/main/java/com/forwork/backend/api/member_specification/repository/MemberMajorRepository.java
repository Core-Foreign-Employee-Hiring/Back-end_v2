package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.MemberMajor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberMajorRepository extends JpaRepository<MemberMajor, Long> {

    @Query("select m.major from MemberMajor m" +
            " where m.memberEducation.id=:memberEducationId")
    List<String> findAllByMemberEducationId(@Param("memberEducationId") Long memberEducationId);

    @Modifying @Transactional
    @Query("delete from MemberMajor mm where mm.memberEducation.id=:memberEducationId")
    void deleteByMemberEducationId(@Param("memberEducationId") Long memberEducationId);
}
