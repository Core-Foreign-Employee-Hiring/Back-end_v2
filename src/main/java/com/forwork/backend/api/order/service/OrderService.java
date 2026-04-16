package com.forwork.backend.api.order.service;

import com.forwork.backend.api.item.entity.Item;
import com.forwork.backend.api.item.repository.ItemRepository;
import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.api.order.dto.request.OrderRequest;
import com.forwork.backend.api.order.dto.response.OrderResponse;
import com.forwork.backend.api.order.entity.Order;
import com.forwork.backend.api.order.entity.OrderItem;
import com.forwork.backend.api.order.repository.OrderItemRepository;
import com.forwork.backend.api.order.repository.OrderRepository;
import com.forwork.backend.common.exception.BadRequestException;
import com.forwork.backend.common.exception.InternalServerException;
import com.forwork.backend.common.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.forwork.backend.common.response.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private static final int MERCHANT_ORDER_THRESHOLD = 10;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public OrderResponse create(Long buyerId, OrderRequest request) {
        Member buyer = memberRepository.findById(buyerId)
                .orElseThrow(() -> {
                    log.warn("[create][멤버 없음.][buyerId={}]", buyerId);
                    return new NotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage());
                });

        List<Long> itemIds = request.itemIds();

        List<Item> items = itemRepository.findActiveItemsByIds(itemIds);

        Set<Long> foundIds = items.stream()
                .map(Item::getId)
                .collect(Collectors.toSet());

        List<Long> invalidIds = itemIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!invalidIds.isEmpty()) {
            log.warn("[create][유효하지 않은 상품][invalidItemIds={}][requestItemIds={}]", invalidIds, itemIds);
            throw new BadRequestException(ORDER_ITEM_INVALID_EXCEPTION.getMessage());
        }


        // merchantOrderId 생성
        String merchantOrderId = MerchantOrderIdGenerator.generate();

        for (int i = 0; i < MERCHANT_ORDER_THRESHOLD; i++) {
            if (orderRepository.existsByMerchantOrderId(merchantOrderId)) {
                merchantOrderId = MerchantOrderIdGenerator.generate();
            } else {
                break;
            }
        }

        // orderName 생성
        String orderName = createOrderName(items);

        // 총 금액
        long sum = items.stream()
                .mapToLong(Item::getPrice)
                .sum();
        String amount = String.valueOf(sum);

        try {
            // order 생성.
            Order order = Order.builder()
                    .merchantOrderId(merchantOrderId)
                    .orderName(orderName)
                    .amount(amount)
                    .buyer(buyer)
                    .build();

            orderRepository.save(order).getId();

        } catch (DataIntegrityViolationException e) {
            // 프론트에게 재시도 유도
            log.warn("[create][중복 merchantOrderId][merchantOrderId={}]", merchantOrderId, e);
            throw new InternalServerException(INTERNAL_SERVER_EXCEPTION.getMessage());
        }

        Order order = orderRepository.findByMerchantOrderId(merchantOrderId).get();

        items.forEach(
                (item) -> {
                    OrderItem orderitem = OrderItem.builder()
                            .price(item.getPrice())
                            .order(order)
                            .item(item)
                            .build();

                    orderItemRepository.save(orderitem);
                });

        OrderResponse response = getOrder(buyerId, merchantOrderId);

        return response;

    }

    private String createOrderName(List<Item> items) {


        String firstProductName = items.get(0).getName();

        if (items.size() == 1) {
            return truncate(firstProductName, 100);
        }

        String orderName = firstProductName + " 외 " + (items.size() - 1) + "건";
        return truncate(orderName, 100);
    }

    private String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength);
    }

    /**
     * 주문 정보 조회
     */
    public OrderResponse getOrder(Long memberId, String merchantOrderId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.warn("[getOrder][멤버 없음.][memberId={}]", memberId);
                    return new NotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage());
                });

        // 주문 조회
        Order order = orderRepository.findByMerchantOrderIdWithBuyer(merchantOrderId)
                .orElseThrow(() -> {
                    log.warn("[getOrder][주문 없음.][merchantOrderId={}]", merchantOrderId);
                    return new NotFoundException(ORDER_NOT_FOUND_EXCEPTION.getMessage());
                });

        // 소유자 검증
        if (!order.getBuyer().getId().equals(memberId)) {
            log.warn("[getOrder][소유자 이상해][memberId={}, buyerId= {}]", memberId, order.getBuyer().getId());
            throw new BadRequestException(ORDER_ACCESS_DENIED_EXCEPTION.getMessage());
        }

        // 상품 온다.
        List<OrderItem> orderItems = orderItemRepository.findByOrderIdWithItem(order.getId());

        Item item = orderItems.get(0).getItem();

        OrderResponse response = OrderResponse.of(member, order, item);

        return response;
    }
}
