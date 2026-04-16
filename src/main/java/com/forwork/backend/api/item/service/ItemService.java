package com.forwork.backend.api.item.service;

import com.forwork.backend.api.item.dto.response.ItemResponse;
import com.forwork.backend.api.item.entity.Item;
import com.forwork.backend.api.item.enums.ItemType;
import com.forwork.backend.common.dto.PageResponseDTO;
import com.forwork.backend.common.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.forwork.backend.common.response.ErrorStatus.INTERNAL_SERVER_EXCEPTION;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final ItemReader itemReader;
    private final List<ItemMapStrategy> itemMapStrategies;


    /*
     * read
     * */

    public PageResponseDTO<ItemResponse> getItems(ItemType itemType, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Item> activeItemsByType = itemReader.findActiveItemsByType(itemType, pageable);

        List<Item> items = activeItemsByType.getContent();


        ItemMapStrategy itemMapStrategy = itemMapStrategies.stream()
                .filter(i -> i.supports(itemType))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("[getItems][전략 없음][itemType= {}]", itemType);
                    return new InternalServerException(INTERNAL_SERVER_EXCEPTION.getMessage());
                });

        List<ItemResponse> map = itemMapStrategy.map(items);

        PageResponseDTO<ItemResponse> response = PageResponseDTO.of(activeItemsByType, map);

        return response;
    }

}
