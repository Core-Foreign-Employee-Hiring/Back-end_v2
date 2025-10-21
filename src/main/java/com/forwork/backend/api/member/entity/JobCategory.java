package com.forwork.backend.api.member.entity;

import lombok.Getter;

import java.util.EnumSet;
import java.util.Set;

@Getter
public enum JobCategory {
    IT_DEVELOPMENT("IT_DEVELOPMENT", "IT/개발", EnumSet.of(
            JobRole.SOFTWARE_ENGINEER,
            JobRole.BACKEND_DEVELOPER,
            JobRole.FRONTEND_DEVELOPER,
            JobRole.JAVA_DEVELOPER,
            JobRole.PYTHON_DEVELOPER,
            JobRole.NODEJS_DEVELOPER,
            JobRole.ANDROID_DEVELOPER,
            JobRole.IOS_DEVELOPER,
            JobRole.DATA_ENGINEER,
            JobRole.MACHINE_LEARNING_ENGINEER,
            JobRole.SECURITY_ENGINEER,
            JobRole.DEVOPS_ENGINEER
    )),

    MARKETING_ADVERTISING("MARKETING_ADVERTISING", "마케팅/광고", EnumSet.of(
            JobRole.MARKETING_MANAGER,
            JobRole.DIGITAL_MARKETER,
            JobRole.CONTENT_MARKETER,
            JobRole.PERFORMANCE_MARKETER,
            JobRole.BRAND_MARKETER,
            JobRole.SNS_MARKETER,
            JobRole.MARKETING_DIRECTOR
    )),

    DESIGN("DESIGN", "디자인", EnumSet.of(
            JobRole.UI_UX_DESIGNER,
            JobRole.WEB_DESIGNER,
            JobRole.GRAPHIC_DESIGNER,
            JobRole.ART_DIRECTOR,
            JobRole.FASHION_DESIGNER,
            JobRole.MOTION_DESIGNER
    )),

    SALES("SALES", "영업", EnumSet.of(
            JobRole.INTERNATIONAL_SALES,
            JobRole.TECHNICAL_SALES,
            JobRole.SALES_ENGINEER,
            JobRole.CUSTOMER_SUCCESS_MANAGER
    )),

    HR("HR", "HR", EnumSet.of(
            JobRole.RECRUITER,
            JobRole.HR_CONSULTANT,
            JobRole.GLOBAL_HR_MANAGER
    )),

    GAME_PRODUCTION("GAME_PRODUCTION", "게임 제작", EnumSet.of(
            JobRole.GAME_PLANNER,
            JobRole.GAME_ARTIST,
            JobRole.GAME_CLIENT_DEVELOPER,
            JobRole.GAME_SERVER_DEVELOPER,
            JobRole.UNITY_DEVELOPER,
            JobRole.UNREAL_DEVELOPER
    ));


    private final String dbValue;
    private final String koreanName;
    private final Set<JobRole> roles;

    JobCategory(String dbValue,String koreanName, Set<JobRole> roles) {
        this.dbValue = dbValue;
        this.koreanName = koreanName;
        this.roles = roles;
    }


    public static JobCategory getCategoryByRole(JobRole role) {
        for (JobCategory category : JobCategory.values()) {
            if (category.roles.contains(role)) {
                return category;
            }
        }
        return null;
    }

    public static JobCategory getCategoryByDbValue(String dbValue) {
        for (JobCategory category : JobCategory.values()) {
            if (category.dbValue.equalsIgnoreCase(dbValue)) {
                return category;
            }
        }
        return null;
    }

    public static Set<JobRole> getRolesByCategory(JobCategory category) {
        return category.roles;
    }
}
