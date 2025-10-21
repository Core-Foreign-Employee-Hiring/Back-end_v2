package com.forwork.backend.api.recruit.service;

import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.member.entity.JobRole;
import com.forwork.backend.api.member.entity.JobRoleEntity;
import com.forwork.backend.api.member.repository.JobRoleEntityRepository;
import com.forwork.backend.api.recruit.dto.request.RecruitUpdateRequestDTO;
import com.forwork.backend.api.member.entity.JobCategoryEntity;
import com.forwork.backend.api.recruit.entity.*;
import com.forwork.backend.api.member.repository.JobCategoryEntityRepository;
import com.forwork.backend.api.recruit.enums.LanguageType;
import com.forwork.backend.api.member.entity.Visa;
import com.forwork.backend.api.recruit.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RecruitUpdater {
    private final RecruitJobCategoryRepository recruitJobCategoryRepository;
    private final JobCategoryEntityRepository JobCategoryEntityRepository;
    private final RecruitJobRoleRepository recruitJobRoleRepository;
    private final JobRoleEntityRepository jobRoleEntityRepository;
    private final RecruitLanguageTypeRepository recruitLanguageTypeRepository;
    private final LanguageTypeEntityRepository languageTypeEntityRepository;
    private final RecruitVisaRepository recruitVisaRepository;
    private final VisaEntityRepository visaEntityRepository;


    public void update(Recruit recruit , RecruitUpdateRequestDTO dto){
        // 직종 수정
        Set<RecruitJobCategory> recruitJobCategories = recruit.getRecruitJobCategories();
        List<JobCategory> oldJobCategories = RecruitUtils.convertToJobCategories(recruitJobCategories);
        List<JobCategory> newJobCategories = dto.jobCategories();

        // add= new-old
        List<JobCategory> doAdd=new ArrayList<>(newJobCategories);

        doAdd.removeAll(oldJobCategories);
        List<JobCategoryEntity> allByJobCategories = JobCategoryEntityRepository.findAllByJobCategories(doAdd.stream().map(JobCategory::getDbValue).toList());

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



        /*
        * 직무
        * */
        Set<RecruitJobRole> recruitJobRoles = recruit.getRecruitJobRoles();
        List<JobRole> oldJobRoles = JobRole.convertToJobRolesByRecruit(recruitJobRoles);
        List<JobRole> newJobRoles = dto.jobRoles().stream().toList();

        // add= new-old
        List<JobRole> doAddJobRoles=new ArrayList<>(newJobRoles);

        doAddJobRoles.removeAll(oldJobRoles);
        List<JobRoleEntity> allByJobRoles = jobRoleEntityRepository.findAllByJobRoles(doAddJobRoles.stream().map(JobRole::getDbValue).toList());

        List<RecruitJobRole> toAddEntityJobRole=new ArrayList<>();
        for (JobRoleEntity allByJobRole : allByJobRoles) {
            RecruitJobRole recruitJobRole = new RecruitJobRole(recruit, allByJobRole);
            toAddEntityJobRole.add(recruitJobRole);

        }

        // delete= old-new
        List<RecruitJobRole>toDeleteJobRole=new ArrayList<>();
        for(RecruitJobRole recruitJobRole : recruitJobRoles){
            if(!newJobRoles.contains(JobRole.getJobRole(recruitJobRole.getJobRoleEntity().getJobRole()))){
                toDeleteJobRole.add(recruitJobRole);
            }
        }

        recruitJobRoleRepository.saveAll(toAddEntityJobRole);
        if(!toDeleteJobRole.isEmpty()){recruitJobRoleRepository.deleteAll(toDeleteJobRole);}


        /*
        * 언어 수정
        * */
        Set<RecruitLanguageType> recruitLanguageTypes = recruit.getRecruitLanguageTypes();
        List<LanguageType> oldLanguageTypes = RecruitUtils.convertToLanguageTypes(recruitLanguageTypes);
        List<LanguageType> newLanguageTypes = dto.languageTypes().stream().toList();

        // add= new-old
        List<LanguageType> doAddLanguageType=new ArrayList<>(newLanguageTypes);

        doAddLanguageType.removeAll(oldLanguageTypes);
        List<LanguageTypeEntity> allByJLanguageTypes = languageTypeEntityRepository.findAllByLanguageTypes(doAddLanguageType.stream().map(LanguageType::getDbValue).toList());

        List<RecruitLanguageType> toAddLanguageTypeEntity=new ArrayList<>();
        for (LanguageTypeEntity allByLanguageType : allByJLanguageTypes) {
            RecruitLanguageType recruitLanguageType = new RecruitLanguageType(recruit, allByLanguageType);
            toAddLanguageTypeEntity.add(recruitLanguageType);

        }

        // delete= old-new
        List<RecruitLanguageType>toDeleteLanguageType=new ArrayList<>();
        for(RecruitLanguageType recruitLanguageType : recruitLanguageTypes){
            if(!newLanguageTypes.contains(LanguageType.getLanguageByDBValue(recruitLanguageType.getLanguageTypeEntity().getLanguageType()))){
                toDeleteLanguageType.add(recruitLanguageType);
            }
        }

        recruitLanguageTypeRepository.saveAll(toAddLanguageTypeEntity);
        if(!toDeleteLanguageType.isEmpty()){recruitLanguageTypeRepository.deleteAll(toDeleteLanguageType);}


        /*
        * 비자 수정
        * */
        Set<RecruitVisa> recruitVisas = recruit.getRecruitVisas();
        List<Visa> oldVisas = RecruitUtils.convertToVisas(recruitVisas);
        List<Visa> newVisas = dto.visas().stream().toList();

        // add= new-old
        List<Visa> doAddVisa=new ArrayList<>(newVisas);

        doAddVisa.removeAll(oldVisas);
        List<VisaEntity> allByVisas = visaEntityRepository.findAllByVisas(doAddVisa.stream().map(Visa::getDbValue).toList());

        List<RecruitVisa> toAddVisaEntity=new ArrayList<>();
        for (VisaEntity allByVisa : allByVisas) {
            RecruitVisa recruitVisa = new RecruitVisa(recruit, allByVisa);
            toAddVisaEntity.add(recruitVisa);

        }

        // delete= old-new
        List<RecruitVisa>toDeleteRecruitVisa=new ArrayList<>();
        for(RecruitVisa recruitVisa : recruitVisas){
            if(!newVisas.contains( Visa.getVisaByDBValue(recruitVisa.getVisaEntity().getVisa()))){
                toDeleteRecruitVisa.add(recruitVisa);
            }
        }

        recruitVisaRepository.saveAll(toAddVisaEntity);
        if(!toDeleteRecruitVisa.isEmpty()){recruitVisaRepository.deleteAll(toDeleteRecruitVisa);}


        // 공고 수정
        recruit.updateFields(dto);
    }
}
