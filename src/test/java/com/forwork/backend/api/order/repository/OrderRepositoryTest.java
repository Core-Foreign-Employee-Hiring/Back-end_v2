package com.forwork.backend.api.order.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderRepositoryTest {


    @Autowired
    private OrderRepository orderRepository;



    @Test
    void test(){
        // give

        // when

        Long memberId=1L;
        boolean b1 = orderRepository.existsPurchasedArchive(memberId, 1L);
        boolean b2 = orderRepository.existsPurchasedArchive(memberId, 2L);
        boolean b3 = orderRepository.existsPurchasedArchive(memberId, 3L);

        // then


        assertTrue(b1);
        assertTrue(b2);
        assertFalse(b3);
    }
}