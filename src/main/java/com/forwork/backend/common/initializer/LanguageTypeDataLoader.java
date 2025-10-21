package com.forwork.backend.common.initializer;

import com.forwork.backend.api.recruit.entity.LanguageTypeEntity;
import com.forwork.backend.api.recruit.enums.LanguageType;
import com.forwork.backend.api.recruit.repository.LanguageTypeEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LanguageTypeDataLoader implements ApplicationRunner {

    private final LanguageTypeEntityRepository entityRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (entityRepository.count() == 0) { // 기존 데이터가 없는 경우만 삽입
            List<LanguageTypeEntity> businessFields = Arrays.stream(LanguageType.values())
                    .map(LanguageTypeEntity::new)
                    .collect(Collectors.toList());

            entityRepository.saveAll(businessFields);
        }
    }
}
