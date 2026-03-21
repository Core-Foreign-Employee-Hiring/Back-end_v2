package com.forwork.backend.api.pass_archive.repository;

import com.forwork.backend.api.pass_archive.entity.ArchiveDownloadHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArchiveDownloadHistoryRepository extends JpaRepository<ArchiveDownloadHistory, Long> {

    @Query("select distinct h.passArchive.passArchiveId from ArchiveDownloadHistory h" +
            " where h.buyer.id=:buyerId")
    List<Long> findPassArchiveIdsByBuyerId(@Param("buyerId") Long buyerId);
}
