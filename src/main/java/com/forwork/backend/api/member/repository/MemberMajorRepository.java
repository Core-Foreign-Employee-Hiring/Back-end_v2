package com.forwork.backend.api.member.repository;

import com.forwork.backend.api.member.entity.MemberMajor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberMajorRepository extends JpaRepository<MemberMajor, Long> {

    @Query("select m.major from MemberMajor m" +
            " where m.memberEducation.id=:memberEducationId")
    List<String> findAllByMemberEducationId(@Param("memberEducationId") Long memberEducationId);
}
