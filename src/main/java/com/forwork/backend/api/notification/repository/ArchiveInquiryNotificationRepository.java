package com.forwork.backend.api.notification.repository;

import com.forwork.backend.api.notification.entity.ArchiveInquiryNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ArchiveInquiryNotificationRepository extends JpaRepository<ArchiveInquiryNotification, Long> {


    @Query("select n from ArchiveInquiryNotification n" +
            " join fetch n.archiveInquiry ai" +
            " join fetch ai.inquirer" +
            " join fetch ai.archive a" +
            " join fetch a.member" +
            " where n.receiver.id=:receiverId" +
            " order by n.id desc")
    List<ArchiveInquiryNotification> findALlByReceiverId(@Param("receiverId")Long receiverId);



    @Transactional @Modifying
    @Query("update ArchiveInquiryNotification n set n.isRead=true where n.receiver.id =:receiverId and n.id in :notificationIds")
    void readByNotificationIds(@Param("receiverId")Long receiverId, @Param("notificationIds") List<Long> notificationIds);
}
