package com.forwork.backend.api.pass_archive.repository;

import com.forwork.backend.api.pass_archive.entity.ArchiveDownloadHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveDownloadHistoryRepository extends JpaRepository<ArchiveDownloadHistory, Long> {
}
