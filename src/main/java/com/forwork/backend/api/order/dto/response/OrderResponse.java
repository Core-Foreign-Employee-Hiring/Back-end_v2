package com.forwork.backend.api.order.dto.response;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.order.entity.Item;
import com.forwork.backend.api.order.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;

public record OrderResponse(
        @Schema(description = "merchantOrderId")
        String merchantOrderId,

        @Schema(description = "orderName")
        String orderName,

        @Schema(description = "상품 이름")
        String itemName,

        @Schema(description = "가격")
        String amount,

        /*
         * 주문자 정보
         * */

        @Schema(description = "이름")
        String name,

        @Schema(description = "휴대폰 번호")
        String phoneNumber,

        @Schema(description = "이메일")
        String email

) {

    public static OrderResponse of(Member buyer, Order order, Item item) {
        return new OrderResponse(
                order.getMerchantOrderId(),
                order.getOrderName(),
                item.getName(),
                order.getAmount(),
                buyer.getName(),
                buyer.getPhoneNumber(),
                buyer.getEmail()
        );
    }
}
