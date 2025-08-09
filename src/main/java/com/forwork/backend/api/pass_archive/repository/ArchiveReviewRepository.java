package com.forwork.backend.api.pass_archive.repository;

import com.forwork.backend.api.pass_archive.entity.ArchiveReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArchiveReviewRepository extends JpaRepository<ArchiveReview, Long> {


    @Query("select ar from ArchiveReview ar" +
            " where ar.passArchive.passArchiveId=:archiveId" +
            " order by ar.id desc")
    Page<ArchiveReview> findAllByArchiveId(@Param("archiveId") Long archiveId, Pageable pageable);

    @Query("select ar from ArchiveReview ar" +
            " join fetch ar.passArchive" +
            " where ar.passArchive.passArchiveId in:archiveIds")
    List<ArchiveReview> findAllByArchiveIds(@Param("archiveIds") List<Long> archiveIds);

    @Query("select count(*)>0 from ArchiveReview ar where ar.writer.id=:writerId and ar.passArchive.passArchiveId=:archiveId")
    boolean existsByWriterIdAndArchiveId(@Param("writerId") Long writerId, @Param("archiveId") Long archiveId);
}
