package com.forwork.backend.api.item.controller;

import com.forwork.backend.api.item.dto.response.ItemResponse;
import com.forwork.backend.api.item.enums.ItemType;
import com.forwork.backend.api.item.service.ItemService;
import com.forwork.backend.common.config.security.SecurityMember;
import com.forwork.backend.common.dto.PageResponseDTO;
import com.forwork.backend.common.response.ApiResponse;
import com.forwork.backend.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Item", description = "상품 관련 API 입니다.")
@RestController
@RequestMapping("/api/v2/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;


    /*
     * read
     * */

    @Operation(
            summary = "상품 조회 API (용범)",
            description = ""
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "상품 조회 성공"),
    })
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<ItemResponse>>> getItems(
            @AuthenticationPrincipal SecurityMember securityMember,

            @Parameter(description = "itemType", in = ParameterIn.QUERY)
            @RequestParam(value = "itemType") ItemType itemType,

            @Parameter(description = "페이지 번호 (0부터 시작)", in = ParameterIn.QUERY)
            @RequestParam(value = "page", defaultValue = "0") Integer page,

            @Parameter(description = "페이지 크기", in = ParameterIn.QUERY)
            @RequestParam(value = "size", defaultValue = "10") Integer size) {


        PageResponseDTO<ItemResponse> response = itemService.getItems(itemType, page, size);

        return ApiResponse.success(SuccessStatus.ITEM_GET_SUCCESS, response);
    }

}
