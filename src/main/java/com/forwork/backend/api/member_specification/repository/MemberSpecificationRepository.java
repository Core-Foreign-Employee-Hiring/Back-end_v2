package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.MemberSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberSpecificationRepository extends JpaRepository<MemberSpecification, Long> {

    @Query("select s from MemberSpecification s" +
            " where s.member.id=:memberId")
    Optional<MemberSpecification> findByMemberIdId(@Param("memberId") Long memberId);


    @Modifying
    @Transactional
    @Query(
            value =
                    """
                            INSERT IGNORE INTO member_specification (member_id, created_at, updated_at) VALUES (?, NOW(), NOW())
                                                                                            
                                                       """,
            nativeQuery = true)
    void insertIgnore(@Param("memberId") Long memberId);
}
