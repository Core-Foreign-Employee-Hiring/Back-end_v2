package com.forwork.backend.api.pass_archive.repository;

import com.forwork.backend.api.pass_archive.entity.PassArchive;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PassArchiveRepository extends JpaRepository<PassArchive, Long> {

    @EntityGraph(attributePaths = {"thumbnail", "images", "member"})
    Optional<PassArchive> findWithThumbnailImagesMemberByPassArchiveId(Long passArchiveId);


    @Query("select pa from PassArchive pa" +
            " left join fetch pa.thumbnail" +
            " where pa.passArchiveId in :ids")
    List<PassArchive> findAllByIds(@Param("ids") List<Long> ids);

    @Query("select pa from PassArchive pa" +
            " join fetch pa.products" +
            " where pa.passArchiveId=:archiveId")
    Optional<PassArchive>findArchiveByArchiveIdWithProducts(@Param("archiveId")Long archiveId);
}
