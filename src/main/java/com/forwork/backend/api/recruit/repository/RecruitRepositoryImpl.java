package com.forwork.backend.api.recruit.repository;

import com.forwork.backend.api.member.entity.Nationality;
import com.forwork.backend.api.recruit.entity.Recruit;
import com.forwork.backend.api.recruit.enums.ContractType;
import com.forwork.backend.api.recruit.enums.LanguageType;
import com.forwork.backend.api.recruit.enums.RecruitPublishStatus;
import com.forwork.backend.api.recruit.enums.WorkRegion;
import com.forwork.backend.api.member.entity.JobRole;
import com.forwork.backend.api.member.entity.Visa;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.forwork.backend.api.member.entity.QJobCategoryEntity.jobCategoryEntity;
import static com.forwork.backend.api.member.entity.QJobRoleEntity.jobRoleEntity;
import static com.forwork.backend.api.recruit.entity.QLanguageTypeEntity.languageTypeEntity;
import static com.forwork.backend.api.recruit.entity.QRecruit.recruit;
import static com.forwork.backend.api.recruit.entity.QRecruitJobCategory.recruitJobCategory;
import static com.forwork.backend.api.recruit.entity.QRecruitJobRole.recruitJobRole;
import static com.forwork.backend.api.recruit.entity.QRecruitLanguageType.recruitLanguageType;
import static com.forwork.backend.api.recruit.entity.QRecruitVisa.recruitVisa;
import static com.forwork.backend.api.recruit.entity.QVisaEntity.visaEntity;

public class RecruitRepositoryImpl implements RecruitRepositoryQueryDSL {
    private final JPAQueryFactory queryFactory;

    public RecruitRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Recruit> getRecruits(String keyword, Pageable pageable,
                                     Set<JobRole> jobRoles, Nationality nationality, Set<LanguageType> languageTypes, Visa visa, Set<WorkRegion> workRegions, ContractType contractType) {

        LocalDate now = LocalDate.now();

        List<Tuple> result = queryFactory.
                selectDistinct(recruit.id, recruit.recruitEndDate)
                .from(recruit)
                .leftJoin(recruit.recruitJobRoles, recruitJobRole)
                .leftJoin(recruit.recruitLanguageTypes, recruitLanguageType)
                .leftJoin(recruit.recruitVisas, recruitVisa)
                .where(
                        recruit.recruitPublishStatus.eq(RecruitPublishStatus.PUBLISHED),
                        recruit.recruitEndDate.goe(now),
                        keywordEq(keyword),
                        jobRoleEq(jobRoles),
                        languageTypeEq(languageTypes),
                        visaEq(visa),
                        workRegionEq(workRegions),
                        contractTypeEq(contractType)
                )
                .orderBy(recruit.recruitEndDate.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> recruitIds = result.stream()
                .map(t -> t.get(recruit.id))  // recruit.id 컬럼만 추출
                .toList();

        if(recruitIds.isEmpty()){
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        List<Recruit> content=queryFactory
                .selectFrom(recruit)
                .leftJoin(recruit.recruitJobCategories, recruitJobCategory).fetchJoin()
                .leftJoin(recruitJobCategory.jobCategoryEntity, jobCategoryEntity).fetchJoin()
                .where(recruit.id.in(recruitIds))
                .orderBy(recruit.recruitEndDate.asc())
                .fetch();

        JPAQuery<Long> countQuery=queryFactory
                .select(recruit.id.countDistinct())
                .from(recruit)
                .leftJoin(recruit.recruitJobRoles, recruitJobRole)
                .leftJoin(recruit.recruitLanguageTypes, recruitLanguageType)
                .leftJoin(recruit.recruitVisas, recruitVisa)
                .where(
                        recruit.recruitPublishStatus.eq(RecruitPublishStatus.PUBLISHED),
                        recruit.recruitEndDate.goe(now),
                        keywordEq(keyword),
                        jobRoleEq(jobRoles),
                        languageTypeEq(languageTypes),
                        visaEq(visa),
                        workRegionEq(workRegions),
                        contractTypeEq(contractType)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

    }


    private BooleanExpression keywordEq(String keyword){
        return (keyword == null || keyword.isEmpty())?null
                : recruit.companyName.contains(keyword).or(recruit.title.contains(keyword));
    }

    private BooleanExpression jobRoleEq(Set<JobRole> jobRoles) {
        if(jobRoles==null || jobRoles.isEmpty()){return null;}


        List<String> list = jobRoles.stream().map(JobRole::getDbValue).toList();

        List<Long> jobRoleIds = queryFactory
                .select(jobRoleEntity.id)
                .from(jobRoleEntity)
                .where(jobRoleEntity.jobRole.in(list))
                .fetch();

        return recruitJobRole.recruit.id.eq(recruit.id).and(recruitJobRole.jobRoleEntity.id.in(jobRoleIds));
    }


    private BooleanExpression languageTypeEq(Set<LanguageType> languageTypes) {
        if(languageTypes==null || languageTypes.isEmpty()){return null;}

        List<String> list = languageTypes.stream().map(LanguageType::getDbValue).toList();

        List<Long> languageTypeIds = queryFactory
                .select(languageTypeEntity.id)
                .from(languageTypeEntity)
                .where(languageTypeEntity.languageType.in(list))
                .fetch();

        return recruitLanguageType.recruit.id.eq(recruit.id).and(recruitLanguageType.languageTypeEntity.id.in(languageTypeIds));
    }

    private BooleanExpression visaEq(Visa visa){
        if(visa==null ){return null;}

        List<Long> visaIds = queryFactory
                .select(visaEntity.id)
                .from(visaEntity)
                .where(visaEntity.visa.in(visa.getDbValue()))
                .fetch();

        return recruitVisa.recruit.id.eq(recruit.id).and(recruitVisa.visaEntity.id.in(visaIds));
    }

    private BooleanExpression workRegionEq(Set<WorkRegion> workRegions) {
        if(workRegions==null || workRegions.isEmpty()){return null;}

        List<String> list = workRegions.stream().map(WorkRegion::getDbValue).toList();

        return recruit.workRegion.in(list);
    }

    private BooleanExpression contractTypeEq(ContractType contractType){
        if(contractType==null){return null;}

        return recruit.contractType.eq(contractType);
    }

}
