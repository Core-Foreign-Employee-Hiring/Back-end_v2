package com.forwork.backend.api.pass_archive.repository;

import com.forwork.backend.api.pass_archive.entity.ArchiveInquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArchiveInquiryRepository extends JpaRepository<ArchiveInquiry, Long> {

    @Query("select ai from ArchiveInquiry ai" +
            " join fetch ai.inquirer" +
            " join fetch ai.archive a" +
            " join fetch a.member" +
            " where ai.id=:archiveInquiryId")
    Optional<ArchiveInquiry> findByIdWithInquirerAndArchiveAndArchiveOwner (@Param("archiveInquiryId")Long archiveInquiryId);


    @Query("select ai from ArchiveInquiry ai" +
            " join fetch ai.inquirer" +
            " join fetch ai.archive a" +
            " join fetch a.member" +
            " where ai.id=:archiveInquiryId")
    Optional<ArchiveInquiry> findByIdWithArchiveAndArchiveWriterAndInquirer (@Param("archiveInquiryId")Long archiveInquiryId);


    @Query("select ai from ArchiveInquiry ai" +
            " join fetch ai.archive a" +
            " join fetch a.member" +
            " where ai.inquirer.id=:inquirerId and a.passArchiveId=:archiveId" +
            " order by ai.id desc" +
            " limit 1")
    Optional<ArchiveInquiry> findLatestInquiryByArchiveInquiryIdAndInquirerId(@Param("inquirerId") Long inquirerId, @Param("archiveId") Long archiveId);

    @Query("select count(*)>0 from ArchiveInquiry ai" +
            " where ai.archive.passArchiveId=:archiveId and ai.isReadByArchiveWriter=false")
    boolean existsUnreadInquiryForArchive(@Param("archiveId") Long archiveId);


    @Query("select ai from ArchiveInquiry ai" +
            " join fetch ai.archive a" +
            " join fetch a.member" +
            " join fetch ai.inquirer" +
            " where ai.inquirer.id=:inquirerId" +
            " order by ai.id desc")
    Page<ArchiveInquiry>findSentInquiriesByInquirerId(@Param("inquirerId") Long inquirerId, Pageable pageable);


    @Query("select ai from ArchiveInquiry ai" +
            " join fetch ai.archive a" +
            " join fetch a.member" +
            " join fetch ai.inquirer" +
            " where a.member.id=:receiverId" +
            " order by ai.id desc")
    Page<ArchiveInquiry>findReceivedInquiriesByReceiverId(@Param("receiverId") Long receiverId, Pageable pageable);

}
