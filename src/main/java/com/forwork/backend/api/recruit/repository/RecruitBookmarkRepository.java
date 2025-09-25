package com.forwork.backend.api.recruit.repository;

import com.forwork.backend.api.recruit.entity.RecruitBookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface RecruitBookmarkRepository extends JpaRepository<RecruitBookmark, Long> {
    @Query("select b from RecruitBookmark b" +
            " where b.recruit.id=:recruitId and b.member.id=:memberId")
    Optional<RecruitBookmark> findByRecruitIdAndMemberId(@Param("recruitId")Long recruitId, @Param("memberId")Long memberId);


    @Query("select b from RecruitBookmark  b" +
            " join fetch b.recruit r" +
            " where b.member.id=:memberId")
    Page<RecruitBookmark> findByMemberId(@Param("memberId")Long memberId, Pageable pageable);

    boolean existsByRecruitIdAndMemberId(Long recruitId, Long memberId);



    @Modifying
    @Transactional
    @Query("delete from RecruitBookmark b where b.recruit.id=:recruitId")
    void deleteByRecruitId(@Param("recruitId") Long recruitId);
}
