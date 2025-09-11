package com.forwork.backend.api.pass_archive.repository;

import com.forwork.backend.api.pass_archive.entity.PassArchive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PassArchiveRepository extends JpaRepository<PassArchive, Long>, PassArchiveQueryDSL {

    @EntityGraph(attributePaths = {"thumbnail", "images", "member"})
    Optional<PassArchive> findWithThumbnailImagesMemberByPassArchiveId(Long passArchiveId);


    @Query("select pa from PassArchive pa" +
            " left join fetch pa.thumbnail" +
            " where pa.passArchiveId in :ids")
    List<PassArchive> findAllByIds(@Param("ids") List<Long> ids);

    @Query("select pa from PassArchive pa" +
            " join fetch pa.member" +
            " where pa.passArchiveId in :archiveIds")
    List<PassArchive> findAllByArchiveIdsWithSeller(@Param("archiveIds") List<Long> archiveIds);

    @Query("select pa from PassArchive pa" +
            " join fetch pa.products" +
            " where pa.passArchiveId=:archiveId")
    Optional<PassArchive>findArchiveByArchiveIdWithProducts(@Param("archiveId")Long archiveId);

    @Query("select pa from PassArchive pa" +
            " join fetch pa.member" +
            " where pa.passArchiveId=:archiveId")
    Optional<PassArchive> findArchiveByArchiveIdWithMember(@Param("archiveId") Long archiveId);

    @Query("select pa from PassArchive pa" +
            " where pa.member.id=:writerId")
    Page<PassArchive> findAllByWriterId(@Param("writerId") Long writerId, Pageable pageable);

    @Modifying
    @Query("update PassArchive pa set pa.star=pa.star+:star where pa.passArchiveId=:passArchiveId")
    void updateStarByPassArchiveId(@Param("passArchiveId")Long passArchiveId, @Param("star")double star);

    @Modifying
    @Query("update PassArchive pa set pa.starCount=pa.starCount+1 where pa.passArchiveId=:passArchiveId")
    void updateStarCountByPassArchiveId(@Param("passArchiveId")Long passArchiveId);
}
