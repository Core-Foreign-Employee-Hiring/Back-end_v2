package com.forwork.backend.api.recruit_review.service;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * db lock 으로 인한 성능 감소에 대비해 게시글의 조회수를 메모리에 올려 관리한다.
 *
 * newCountMap: tacker 가 관리하는 게시글의 조회수 key: recruitReviewId, value: readCount
 * preCountMap: key: recruitReviewId value: PreData.count: 마지막 flush 시 조회수,  PreData.createdTime: 게시글 생성 시간.
 *
 * preCountMap 도입 이유.
 *
 * 모든 글을 tracker 가 관리하기엔 메모리 사용에 있어서 비율적임.
 * db lock 으로 인한 성능 저하가 발생하는 게시글은 인기 있는 특정 게시글들임.
 * 인기 없는 게시글 삭제를 위한 자료구조.
 *
 * 자세한 동작은 ReadCountFlusher.flush() 참고.
 */

//@Component
public class ReadCountTracker {
    private final ConcurrentHashMap<Long, AtomicInteger> newCountMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, PreData> preCountMap = new ConcurrentHashMap<>();


    /**
     * 조회수를 추적할 게시글을 tracker 에 등록.
     */

    public void init(Long recruitReviewId, LocalDateTime createdAt) {
        newCountMap.put(recruitReviewId, new AtomicInteger(0));
        preCountMap.put(recruitReviewId, new PreData(0, createdAt));
    }

    /**
     * 해당 게시글이 tacker 에 있을 경우 1 증가시키고 리턴
     * 없을 경우 -1을 리턴해준다.
     *  -1로 리턴할 경우 직접 db에 update 해줘야 한다.
     */

    public int increment(Long recruitReviewId) {
        AtomicInteger counter = newCountMap.get(recruitReviewId);
        if (counter != null) {
            return counter.incrementAndGet();
        }
        return -1;
    }

    /**
     * 게시글들의 조회수를 리턴.
     *  tracker 가 관리하지 않는 게시글이라면 -1로 리턴한다.
     *  -1 일 경우 db 에서 조회해서 조회수 사용.
     */

    public Map<Long, Integer> getReadCounts(List<Long> recruitReviewIds) {
        Map<Long, Integer> result = new HashMap<>();
        for (Long recruitReviewId : recruitReviewIds) {
            AtomicInteger counter = newCountMap.get(recruitReviewId);
            result.put(recruitReviewId, counter != null ? counter.get() : -1);
        }
        return result;
    }

    /**
     * flush 를 위해 현재 tracker 가 관리하는 조회수를 반환한다.
     */

    public Map<Long, Integer> drainAll() {
        Map<Long, Integer> result = new HashMap<>();
        for (Map.Entry<Long, AtomicInteger> entry : newCountMap.entrySet()) {
            result.put(entry.getKey(), entry.getValue().get());
        }
        return result;
    }

    /**
     * 마지막으로 flush 한 조회수를 기록한다.
     */

    public void updatePreCount(Long postId, int newCount) {
        PreData old = preCountMap.get(postId);
        if (old != null) {
            preCountMap.put(postId, new PreData(newCount, old.createdTime));
        }
    }

    public PreData getPreData(Long postId) {
        return preCountMap.get(postId);
    }


    /**
     * 해당 게시글은 tracker 에서 삭제.
     */

    public void remove(Long postId) {
        newCountMap.remove(postId);
        preCountMap.remove(postId);
    }

    public static class PreData {
        public final int count; // 마지막 flush 시 조회수
        public final LocalDateTime createdTime; // 게시글 생성 시간

        public PreData(int count, LocalDateTime createdTime) {
            this.count = count;
            this.createdTime = createdTime;
        }
    }
}
