package com.forwork.backend.api.recruit.service;

import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.recruit.entity.RecruitJobCategory;
import com.forwork.backend.api.recruit.entity.RecruitLanguageType;
import com.forwork.backend.api.recruit.entity.RecruitVisa;
import com.forwork.backend.api.recruit.enums.LanguageType;
import com.forwork.backend.api.member.entity.Visa;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RecruitUtils {

    public static List<JobCategory> convertToJobCategories(Set<RecruitJobCategory> recruitJobCategories){
        List<JobCategory> jobCategories = new ArrayList<>();
        for (RecruitJobCategory recruitJobCategory : recruitJobCategories) {
            String jobCategory = recruitJobCategory.getJobCategoryEntity().getJobCategory();
            JobCategory categoryByDbValue = JobCategory.getCategoryByDbValue(jobCategory);
            jobCategories.add(categoryByDbValue);
        }
        return jobCategories;
    }

    public static List<LanguageType> convertToLanguageTypes(Set<RecruitLanguageType> recruitLanguageTypes){
        List<LanguageType> languageTypes = new ArrayList<>();
        for (RecruitLanguageType recruitLanguageType : recruitLanguageTypes) {
            String languageType = recruitLanguageType.getLanguageTypeEntity().getLanguageType();
            LanguageType languageByDBValue = LanguageType.getLanguageByDBValue(languageType);

            languageTypes.add(languageByDBValue);
        }
        return languageTypes;
    }

    public static List<Visa> convertToVisas(Set<RecruitVisa> recruitVisas){
        List<Visa> visas = new ArrayList<>();
        for (RecruitVisa recruitVisa : recruitVisas) {
            String visa = recruitVisa.getVisaEntity().getVisa();
            Visa visaByDBValue = Visa.getVisaByDBValue(visa);

            visas.add(visaByDBValue);
        }
        return visas;
    }
}
