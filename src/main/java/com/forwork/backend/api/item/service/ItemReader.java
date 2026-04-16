package com.forwork.backend.api.item.service;

import com.forwork.backend.api.item.entity.Item;
import com.forwork.backend.api.item.enums.ItemType;
import com.forwork.backend.api.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemReader {
    private final ItemRepository itemRepository;

    public Page<Item> findActiveItemsByType(ItemType itemType, Pageable pageable) {
        return itemRepository.findActiveItemsByType(itemType.getValue(), pageable);
    }
}
