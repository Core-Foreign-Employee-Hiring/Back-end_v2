package com.forwork.backend.api.member.repository;

import com.forwork.backend.api.member.entity.MemberJobRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberJobRoleRepository extends JpaRepository<MemberJobRole, Long> {

    @Query("select mjr from MemberJobRole mjr" +
            " left join mjr.jobRoleEntity" +
            " where mjr.member.id=:memberId")
    List<MemberJobRole> findByMemberId(@Param("memberId")Long memberId);
}
