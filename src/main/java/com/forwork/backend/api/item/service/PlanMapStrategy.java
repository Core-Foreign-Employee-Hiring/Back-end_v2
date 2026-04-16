package com.forwork.backend.api.item.service;

import com.forwork.backend.api.item.dto.response.ItemResponse;
import com.forwork.backend.api.item.dto.response.PlanResponse;
import com.forwork.backend.api.item.entity.Item;
import com.forwork.backend.api.item.enums.ItemType;
import com.forwork.backend.api.plan.entity.PlanVersion;
import com.forwork.backend.api.plan.service.PlanReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlanMapStrategy implements ItemMapStrategy {
    private final PlanReader planReader;

    @Override
    public boolean supports(ItemType itemType) {
        return ItemType.PLAN.equals(itemType);
    }

    @Override
    public List<ItemResponse> map(List<Item> items) {
        List<Long> itemIds = items.stream().map(Item::getId).toList();

        List<PlanVersion> plans = planReader.getPlansByItemIds(itemIds);

        List<ItemResponse> response = plans.stream()
                .<ItemResponse>map(PlanResponse::of)
                .toList();

        return response;
    }
}
