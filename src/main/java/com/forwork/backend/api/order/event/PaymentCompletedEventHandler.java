package com.forwork.backend.api.order.event;

import com.forwork.backend.api.item.entity.Item;
import com.forwork.backend.api.item.enums.ItemType;
import com.forwork.backend.api.order.service.OrderReader;
import com.forwork.backend.api.pay.event.PaymentCompletedEvent;
import com.forwork.backend.api.plan.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentCompletedEventHandler {
    private final OrderReader orderReader;
    private final SubscriptionService subscriptionService;


    @Async
    @EventListener
    public void handle(PaymentCompletedEvent event) {
        List<Item> items = orderReader.getItems(event.merchantOrderId());

        for (Item item : items) {
            if (item.getType().equals(ItemType.PLAN.getValue())) {
                subscriptionService.upgradeToPro(event.memberId());
            }
        }

    }
}
