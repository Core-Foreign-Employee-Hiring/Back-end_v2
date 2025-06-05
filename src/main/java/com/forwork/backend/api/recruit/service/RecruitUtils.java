package com.forwork.backend.api.recruit.service;

import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.recruit.entity.RecruitJobCategory;

import java.util.ArrayList;
import java.util.List;

public class RecruitUtils {

    public static List<JobCategory> convertToJobCategories(List<RecruitJobCategory> recruitJobCategories){
        List<JobCategory> jobCategories = new ArrayList<>();
        for (RecruitJobCategory recruitJobCategory : recruitJobCategories) {
            JobCategory jobCategory = recruitJobCategory.getJobCategoryEntity().getJobCategory();
            jobCategories.add(jobCategory);
        }
        return jobCategories;
    }
}
