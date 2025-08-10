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
            " join fetch ai.archive a" +
            " where ai.id=:archiveInquiryId")
    Optional<ArchiveInquiry> findByIdWithArchive (@Param("archiveInquiryId")Long archiveInquiryId);


    @Query("select ai from ArchiveInquiry ai" +
            " join fetch ai.archive" +
            " where ai.inquirer.id=:inquirerId" +
            " order by ai.id desc")
    Page<ArchiveInquiry>findSentInquiriesByInquirerId(@Param("inquirerId") Long inquirerId, Pageable pageable  );


    @Query("select ai from ArchiveInquiry ai" +
            " join fetch ai.archive a" +
            " where a.member.id=:receiverId" +
            " order by ai.id desc")
    Page<ArchiveInquiry>findReceivedInquiriesByReceiverId(@Param("receiverId") Long receiverId, Pageable pageable);

}
