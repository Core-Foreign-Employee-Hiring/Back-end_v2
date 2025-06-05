package com.forwork.backend.common.initializer;

import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.recruit.entity.JobCategoryEntity;
import com.forwork.backend.api.recruit.repository.JobCategoryEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JobCategoryDataLoader implements ApplicationRunner {

    private final JobCategoryEntityRepository entityRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (entityRepository.count() == 0) { // 기존 데이터가 없는 경우만 삽입
            List<JobCategoryEntity> businessFields = Arrays.stream(JobCategory.values())
                    .map(JobCategoryEntity::new)
                    .collect(Collectors.toList());

            entityRepository.saveAll(businessFields);
        }
    }
}

