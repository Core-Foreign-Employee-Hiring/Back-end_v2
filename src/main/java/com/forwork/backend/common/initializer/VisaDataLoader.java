package com.forwork.backend.common.initializer;

import com.forwork.backend.api.recruit.entity.VisaEntity;
import com.forwork.backend.api.recruit.repository.VisaEntityRepository;
import com.forwork.backend.api.member.entity.Visa;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VisaDataLoader implements ApplicationRunner {

    private final VisaEntityRepository entityRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (entityRepository.count() == 0) { // 기존 데이터가 없는 경우만 삽입
            List<VisaEntity> businessFields = Arrays.stream(Visa.values())
                    .map(VisaEntity::new)
                    .collect(Collectors.toList());

            entityRepository.saveAll(businessFields);
        }
    }
}
