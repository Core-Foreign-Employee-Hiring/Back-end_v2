package com.forwork.backend.api.recruit_review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
@Slf4j
public class FlushScheduler {
    private final ReadCountFlusher flusher;


    @Scheduled(fixedDelay = 3000) // ms
    public void scheduledFlush() {
        log.info("[ReadCountFlusher][begin]");
        flusher.flush();
        log.info("[ReadCountFlusher][end]");
    }
}
