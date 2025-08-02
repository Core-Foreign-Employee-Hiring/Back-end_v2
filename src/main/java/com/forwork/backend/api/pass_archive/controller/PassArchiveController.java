package com.forwork.backend.api.pass_archive.controller;

import com.forwork.backend.api.pass_archive.dto.PassArchiveCreateRequestDTO;
import com.forwork.backend.api.pass_archive.dto.PassArchiveDetailResponseDTO;
import com.forwork.backend.api.pass_archive.service.PassArchiveService;
import com.forwork.backend.common.config.security.SecurityMember;
import com.forwork.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.forwork.backend.common.response.SuccessStatus;

import java.util.List;

@Tag(name = "PassArchive", description = "합격 아카이브 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/pass-archives")
@RequiredArgsConstructor
public class PassArchiveController {

    private final PassArchiveService passArchiveService;

    @Operation(summary = "합격아카이브 등록 (태근)", description = "무료일 경우 price를 0으로 넘겨주세요 / thumbnail : 썸네일 , images : 본문 이미지들, products : 판매할 상품들")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "합격 아카이브 등록 성공"),
    })
    @PostMapping(
            value = "",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<Long>> create(
            @RequestPart("data") PassArchiveCreateRequestDTO passArchiveCreateRequestDTO,
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "products") List<MultipartFile> products,
            @AuthenticationPrincipal SecurityMember securityMember) {

        Long id = passArchiveService.createArchive(passArchiveCreateRequestDTO, thumbnail, images, products, securityMember.getId());

        return ApiResponse.success(SuccessStatus.CREATE_PASS_ARCHIVE_SUCCESS, id);
    }

    @Operation(summary = "합격아카이브 상세 조회 (태근)", description = "ID로 합격아카이브 상세 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "합격 아카이브 상세 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "합격 아카이브를 찾을 수 없습니다.")
    })
    @GetMapping("/detail")
    public ResponseEntity<ApiResponse<PassArchiveDetailResponseDTO>> getDetail(@RequestParam("id") Long id) {

        PassArchiveDetailResponseDTO passArchiveDetailResponseDTO = passArchiveService.getDetailArchive(id);
        return ApiResponse.success(SuccessStatus.SEND_PASS_ARCHIVE_DETAIL_SUCCESS, passArchiveDetailResponseDTO);
    }
}