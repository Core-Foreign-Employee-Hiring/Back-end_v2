package com.forwork.backend.api.notification.service;

import com.forwork.backend.api.notification.dto.reponse.ArchiveInquiryNotificationResponseDTO;
import com.forwork.backend.api.notification.entity.ArchiveInquiryNotification;
import com.forwork.backend.api.notification.repository.ArchiveInquiryNotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArchiveInquiryNotificationService {
    private final ArchiveInquiryNotificationRepository archiveInquiryNotificationRepository;


    /*
    * r
    * */

    /**
     * 알람 조회
     */

    public List<ArchiveInquiryNotificationResponseDTO> getArchiveNotification(Long receiverId){
        // 알람 조회
        List<ArchiveInquiryNotification> notifications = archiveInquiryNotificationRepository.findALlByReceiverId(receiverId);

        // 변환
        List<ArchiveInquiryNotificationResponseDTO> response = notifications.stream()
                .map(ArchiveInquiryNotificationResponseDTO::of)
                .toList();

        return response;
    }


    /*
    * u
    * */

    /**
     * 알림 읽음 처리
     */

    @Transactional
    public void read(Long receiverId, List<Long> notificationIds){
        archiveInquiryNotificationRepository.readByNotificationIds(receiverId, notificationIds);
    }

}
