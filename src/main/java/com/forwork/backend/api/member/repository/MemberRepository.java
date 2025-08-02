package com.forwork.backend.api.member.repository;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.member.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserId(String userId);
    Optional<Member> findByRefreshToken(String refreshToken);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUserIdAndEmailAndName(String userId, String email, String name);
    Optional<Member> findByNameAndPhoneNumber(String name, String phoneNumber);
    Optional<Member> findByPhoneNumber(String phoneNumber);

    @Query("select m from Member m" +
            " where m.id=:memberId and m.role=:role")
    Optional<Member> findByMemberIdAndRole(@Param("memberId")Long memberId, @Param("role") Role role );

    @Modifying
    @Query("update Member m set m.userId = :userId where m.id = :memberId")
    void updateUserId(@Param("memberId") Long memberId, @Param("userId") String userId);
}