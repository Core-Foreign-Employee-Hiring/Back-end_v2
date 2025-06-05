package com.forwork.backend.api.recruit.service;

import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.recruit.dto.request.RecruitUpdateRequestDTO;
import com.forwork.backend.api.recruit.entity.JobCategoryEntity;
import com.forwork.backend.api.recruit.entity.Recruit;
import com.forwork.backend.api.recruit.entity.RecruitJobCategory;
import com.forwork.backend.api.recruit.repository.JobCategoryEntityRepository;
import com.forwork.backend.api.recruit.repository.RecruitJobCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RecruitUpdater {
    private final RecruitJobCategoryRepository recruitJobCategoryRepository;
    private final JobCategoryEntityRepository JobCategoryEntityRepository;


    public void update(Recruit recruit , RecruitUpdateRequestDTO dto){
        // 직종 수정
        List<RecruitJobCategory> recruitJobCategories = recruit.getRecruitJobCategories();
        List<JobCategory> oldJobCategories = RecruitUtils.convertToJobCategories(recruitJobCategories);
        List<JobCategory> newJobCategories = dto.jobCategories();

        // add= new-old
        List<JobCategory> doAdd=new ArrayList<>(newJobCategories);

        doAdd.removeAll(oldJobCategories);
        List<JobCategoryEntity> allByJobCategories = JobCategoryEntityRepository.findAllByJobCategories(doAdd);

        List<RecruitJobCategory> toAddEntity=new ArrayList<>();
        for (JobCategoryEntity allByJobCategory : allByJobCategories) {
            RecruitJobCategory recruitJobCategory = new RecruitJobCategory(recruit, allByJobCategory);
            toAddEntity.add(recruitJobCategory);

        }

        // delete= old-new
        List<RecruitJobCategory>toDelete=new ArrayList<>();
        for(RecruitJobCategory recruitJobCategory : recruitJobCategories){
            if(!newJobCategories.contains(recruitJobCategory.getJobCategoryEntity().getJobCategory())){
                toDelete.add(recruitJobCategory);
            }
        }

        recruitJobCategoryRepository.saveAll(toAddEntity);
        if(!toDelete.isEmpty()){recruitJobCategoryRepository.deleteAll(toDelete);}


        // 공고 수정
        recruit.updateFields(dto);
    }
}
