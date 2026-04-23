package com.forwork.backend.api.plan.service;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.member.service.MemberReader;
import com.forwork.backend.api.plan.dto.response.PlanResponse;
import com.forwork.backend.api.plan.entity.PlanVersion;
import com.forwork.backend.api.plan.entity.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final SubscriptionCreator subscriptionCreator;
    private final SubscriptionReader subscriptionReader;
    private final SubscriptionUpdater subscriptionUpdater;
    private final PlanReader planReader;
    private final MemberReader memberReader;


    /*
     * reade
     * */

    /**
     * 유저의 plan 조회
     */
    public PlanResponse getPlan(Long memberId) {
        Subscription subscription = subscriptionReader.getActiveSubscription(memberId);

        PlanResponse response = PlanResponse.of(subscription);

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


    /*
     * lifecycle
     */
    @Transactional
    public void downgradeExpiredSubscriptions() {
        List<Subscription> subscriptionsExpiredYesterday = subscriptionReader.findSubscriptionsExpiredYesterday();

        // 위에 있는 거 비활성화 해야 함.
        List<Long> subscriptionIds = subscriptionsExpiredYesterday.stream()
                .map(Subscription::getId)
                .toList();

        subscriptionUpdater.processExpiration(subscriptionIds);

        /*
         * FREE 로 변경
         * */


        PlanVersion freePlan = planReader.getFreePlan();


        subscriptionsExpiredYesterday.forEach((subscription) -> {
            Member member = subscription.getMember();

            subscriptionCreator.createSubscription(member, freePlan);
        });

    }
}
