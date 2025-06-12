package com.forwork.backend.api.recruit_review.service;

import com.forwork.backend.api.recruit_review.repository.RecruitReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

//@Component
@RequiredArgsConstructor
public class ReadCountFlusher {
    private final ReadCountTracker tracker;
    private final RecruitReviewRepository recruitReviewRepository;
    private final int THRESHOLD = 10;                                               // 인기없는 글 판단을 위한 THRESHOLD 값.    flush 주기 동안 THRESHOLD 이상인 게시글만 tracker 에 유지한다.
    private static final Duration POPULARITY_GRACE_PERIOD = Duration.ofMinutes(10); // 게시글이 '인기글'로 분류될 유예 기간

    /**
     * tracker 에서 조회수들을 갖고 와 DB에 반영한다.
     *
     * 메모리 효율을 위해 조회수 변화량이 적은 글은 tacker 에서 제거.
     *
     * 조회수 변화량이 적은 글: 게시글이 생성된지 POPULARITY_GRACE_PERIOD 이상이지만 조회수 변화량이 THRESHOLD 이하인 글로 정의.
     * POPULARITY_GRACE_PERIOD 와 THRESHOLD 애플리케이션(게시판) 특성에 맞게 적절하게 설정.
     *
     * 현재 인기글 기능은 없지만 해당 기능 추가 시 메모리 효율 증가 가능.
     * 현재 tacker 가 생성된 모든 글을 관리하지만, 인기글 기능 도입 시 일반글 ->인기글로 진화한 글만 관리하면 됨.
     *
     *
     *
     * 조회수 손실 문제.
     *
     * T1: 게시글 조회로 tacker 에서 조회수 증가
     * T2: tracker.remove(postId); 실행.
     *
     * 해당 상황 시 조회수 손실 발생 가능.
     * 현재 조회수 관련 중요한 요구사항이 없고 손실이 발생한 글은 애초에 조회수 변화량이 적은 글이라 크게 문제될 거 없다고 판단.
     * 만약, 중요하다고 판단 시 메시지큐 도입 혹은 트랜잭션 아웃박스 패턴 등 이용 가능.
     *
     */

    public void flush() {
        LocalDateTime now = LocalDateTime.now();
        Map<Long, Integer> newCounts = tracker.drainAll();

        for (Map.Entry<Long, Integer> entry : newCounts.entrySet()) {
            Long postId = entry.getKey();
            int newCount = entry.getValue();

            ReadCountTracker.PreData preData = tracker.getPreData(postId);
            if (preData == null) continue;

            int preCount = preData.count;
            LocalDateTime createdAt = preData.createdTime;

            int delta = newCount - preCount;

            // 조회수가 변한 글만 DB 반영
            if (delta > 0) {
                tracker.updatePreCount(postId, newCount);
                recruitReviewRepository.updateViewCount(postId, newCount);
            }

            // 조회수 변화량이 적은 글이면 삭제해준다.
            if (delta <= THRESHOLD && createdAt.isBefore(now.minus(POPULARITY_GRACE_PERIOD))) {
                tracker.remove(postId);
            }
        }
    }
}
