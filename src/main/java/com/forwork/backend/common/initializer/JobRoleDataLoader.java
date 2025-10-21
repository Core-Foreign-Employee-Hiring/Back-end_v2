package com.forwork.backend.common.initializer;

import com.forwork.backend.api.member.entity.JobRoleEntity;
import com.forwork.backend.api.member.repository.JobRoleEntityRepository;
import com.forwork.backend.api.member.entity.JobRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JobRoleDataLoader implements ApplicationRunner {

    private final JobRoleEntityRepository entityRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (entityRepository.count() == 0) { // 기존 데이터가 없는 경우만 삽입
            List<JobRoleEntity> businessFields = Arrays.stream(JobRole.values())
                    .map(JobRoleEntity::new)
                    .collect(Collectors.toList());

            entityRepository.saveAll(businessFields);
        }
    }
}
