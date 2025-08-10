package com.forwork.backend.api.pass_archive.repository;

import com.forwork.backend.api.pass_archive.entity.PassArchive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PassArchiveQueryDSL {
    Page<PassArchive> getPassArchives(String keyword, Pageable pageable);
}
