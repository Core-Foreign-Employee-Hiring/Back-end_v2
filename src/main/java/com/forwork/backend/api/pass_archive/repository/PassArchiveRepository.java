package com.forwork.backend.api.pass_archive.repository;

import com.forwork.backend.api.pass_archive.entity.PassArchive;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassArchiveRepository extends JpaRepository<PassArchive, Long> {

    @EntityGraph(attributePaths = {"thumbnail", "images", "member"})
    Optional<PassArchive> findWithThumbnailImagesMemberByPassArchiveId(Long passArchiveId);
}
