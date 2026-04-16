package com.forwork.backend.api.item.service;

import com.forwork.backend.api.item.dto.response.ItemResponse;
import com.forwork.backend.api.item.entity.Item;
import com.forwork.backend.api.item.enums.ItemType;

import java.util.List;

public interface ItemMapStrategy {
    boolean supports(ItemType itemType);

    List<ItemResponse> map(List<Item> items);
}
