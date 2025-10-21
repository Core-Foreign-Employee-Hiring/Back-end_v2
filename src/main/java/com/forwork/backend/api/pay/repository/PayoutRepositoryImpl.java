package com.forwork.backend.api.pay.repository;

import com.forwork.backend.api.pay.entity.Payout;
import com.forwork.backend.api.pay.enums.PayoutStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.Collections;
import java.util.List;

import static com.forwork.backend.api.pay.entity.QPayout.payout;

public class PayoutRepositoryImpl implements PayoutRepositoryQueryDSL {

    private final JPAQueryFactory queryFactory;

    public PayoutRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Payout> findBySellerIdAndPayoutStatus(Long sellerId, PayoutStatus payoutStatus, Pageable pageable) {
        List<Payout> content = queryFactory.selectFrom(payout)
                .join(payout.seller).fetchJoin()
                .where(
                        sellerIdEq(sellerId),
                        payoutStatusEq(payoutStatus)
                )
                .orderBy(payout.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if(content.isEmpty()){
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        JPAQuery<Long> countQuery = queryFactory
                .select(payout.count())
                .from(payout)
                .join(payout.seller)
                .where(
                        sellerIdEq(sellerId),
                        payoutStatusEq(payoutStatus)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

    }

    private BooleanExpression sellerIdEq(Long sellerId){
        return (sellerId == null)?null
                : payout.seller.id.eq(sellerId);
    }

    private BooleanExpression payoutStatusEq(PayoutStatus payoutStatus){
        return (payoutStatus == null)?null
                : payout.payoutStatus.eq(payoutStatus);
    }
}
