package com.forwork.backend.api.notification.controller;

import com.forwork.backend.api.notification.dto.reponse.ArchiveInquiryNotificationResponseDTO;
import com.forwork.backend.api.notification.dto.request.ArchiveInquiryNotificationReadRequestDTO;
import com.forwork.backend.api.notification.service.ArchiveInquiryNotificationService;
import com.forwork.backend.common.config.security.SecurityMember;
import com.forwork.backend.common.response.ApiResponse;
import com.forwork.backend.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Notification", description = "알람 관련 API 입니다.")
@RestController
@RequestMapping("/api/v2/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final ArchiveInquiryNotificationService archiveInquiryNotificationService;



    /*
     * r
     * */

    @Operation(summary = "아카이브 문의 알람 조희 (용범)", description = "출력: ArchiveInquiryNotificationResponseDTO")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "아카이브 문의 알람 조회 성공"),
    })
    @GetMapping("/archive-inquiries")
    public ResponseEntity<ApiResponse<List<ArchiveInquiryNotificationResponseDTO>>> getArchiveNotification(@AuthenticationPrincipal SecurityMember securityMember) {

        List<ArchiveInquiryNotificationResponseDTO> response = archiveInquiryNotificationService.getArchiveNotification(securityMember.getId());
        return ApiResponse.success(SuccessStatus.SEND_ARCHIVE_INQUIRY_NOTIFICATIONS_SUCCESS, response);
    }


    /*
     * u
     * */

    @Operation(summary = "아카이브 문의 알람 읽음 처리 (용범)", description = "입력: ArchiveInquiryNotificationReadRequestDTO")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "알림 읽음 처리 성공"),
    })
    @PatchMapping("/archive-inquiries/read")
    public ResponseEntity<ApiResponse<Void>> read(@AuthenticationPrincipal SecurityMember securityMember, @RequestBody ArchiveInquiryNotificationReadRequestDTO dto) {

        archiveInquiryNotificationService.read(securityMember.getId(), dto.notificationIds());
        return ApiResponse.success_only(SuccessStatus.READ_NOTIFICATIONS_SUCCESS);
    }
}
