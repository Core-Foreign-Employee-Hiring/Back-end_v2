package com.forwork.backend.api.order.service;

import com.forwork.backend.api.item.entity.Item;
import com.forwork.backend.api.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderReader {
    private final ItemRepository itemRepository;


    /**
     * 아이템 조회
     */
    public List<Item> getItems(String merchantOrderId) {
        List<Item> items = itemRepository.findItemsByMerchantOrderId(merchantOrderId);
        return items;
    }
}
