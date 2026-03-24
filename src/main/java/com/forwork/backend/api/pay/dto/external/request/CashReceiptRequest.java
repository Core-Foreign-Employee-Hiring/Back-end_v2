package com.forwork.backend.api.pay.dto.external.request;

import com.forwork.backend.api.order.entity.Order;

public record CashReceiptRequest(
        Integer amount,
        String orderId,
        String orderName,
        String type,
        String customerIdentityNumber,
        Integer taxFreeAmount
) {

    public static CashReceiptRequest of(Integer amount, String merchantOrderId, String orderName,
                                        String type, String customerIdentityNumber, Integer taxFreeAmount) {

        return new CashReceiptRequest(
                amount,
                merchantOrderId,
                orderName,
                type,
                customerIdentityNumber,
                taxFreeAmount

        );
    }

    public static CashReceiptRequest of(Order order, String type, String customerIdentityNumber, Integer taxFreeAmount) {

        return new CashReceiptRequest(
                Integer.valueOf(order.getAmount()),
                order.getMerchantOrderId(),
                order.getOrderName(),
                type,
                customerIdentityNumber,
                taxFreeAmount

        );
    }
}
