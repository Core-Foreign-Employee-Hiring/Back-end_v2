package com.forwork.backend.api.plan.service;

import com.forwork.backend.api.plan.dto.response.MyPlanResponse;
import com.forwork.backend.api.plan.entity.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final SubscriptionReader subscriptionReader;


    /*
     * reader
     * */

    /**
     * 유저의 plan 조회
     */
    public MyPlanResponse getMyPlan(Long memberId) {
        Subscription subscription = subscriptionReader.getActiveSubscription(memberId);

        MyPlanResponse response = MyPlanResponse.of(subscription);

        return response;
    }
}
