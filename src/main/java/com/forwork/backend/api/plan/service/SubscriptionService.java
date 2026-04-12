package com.forwork.backend.api.plan.service;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.member.service.MemberReader;
import com.forwork.backend.api.plan.dto.response.MyPlanResponse;
import com.forwork.backend.api.plan.entity.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final SubscriptionCreator subscriptionCreator;
    private final SubscriptionReader subscriptionReader;
    private final SubscriptionUpdater subscriptionUpdater;
    private final MemberReader memberReader;


    /*
     * reade
     * */

    /**
     * 유저의 plan 조회
     */
    public MyPlanResponse getMyPlan(Long memberId) {
        Subscription subscription = subscriptionReader.getActiveSubscription(memberId);

        MyPlanResponse response = MyPlanResponse.of(subscription);

        return response;
    }



    /*
     * update
     * */

    /**
     * 유저 PRO로 플랜 변경
     */
    @Transactional
    public void upgradeToPro(Long memberId) {

        // 현재 유저의 플랜 비활성화
        subscriptionUpdater.replaceActiveSubscription(memberId);

        // 새로운 플랜 생성
        Member member = memberReader.getMember(memberId);
        subscriptionCreator.createProSubscription(member);
    }
}
