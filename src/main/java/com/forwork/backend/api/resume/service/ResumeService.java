package com.forwork.backend.api.resume.service;

import com.forwork.backend.api.aws.service.S3Service;
import com.forwork.backend.api.file.FileDirAndName;
import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.member.entity.MemberJobRole;
import com.forwork.backend.api.member.repository.MemberJobRoleRepository;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.api.member_specification.entity.*;
import com.forwork.backend.api.member_specification.repository.*;
import com.forwork.backend.api.resume.dto.ResumeCreateRequest;
import com.forwork.backend.api.resume.dto.ResumeCreateResponse;
import com.forwork.backend.api.resume.dto.ResumeDetailResponse;
import com.forwork.backend.api.resume.dto.ResumeListResponse;
import com.forwork.backend.api.resume.dto.ResumeSelectionRequest;
import com.forwork.backend.api.resume.entity.Resume;
import com.forwork.backend.api.resume.entity.ResumeUrl;
import com.forwork.backend.api.resume.repository.ResumeRepository;
import com.forwork.backend.api.resume.repository.ResumeUrlRepository;
import com.forwork.backend.common.exception.BadRequestException;
import com.forwork.backend.common.exception.ForbiddenException;
import com.forwork.backend.common.exception.NotFoundException;
import com.forwork.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final ResumeUrlRepository resumeUrlRepository;
    private final MemberRepository memberRepository;
    private final MemberJobRoleRepository memberJobRoleRepository;
    private final S3Service s3Service;

    private final MemberSpecificationRepository memberSpecificationRepository;
    private final MemberEducationRepository memberEducationRepository;
    private final MemberMajorRepository memberMajorRepository;
    private final MemberCertificationRepository memberCertificationRepository;
    private final MemberLanguageSkillRepository memberLanguageSkillRepository;
    private final MemberCareerRepository memberCareerRepository;
    private final MemberAwardRepository memberAwardRepository;
    private final MemberExperienceRepository memberExperienceRepository;

    // 이력서 초안 생성
    @Transactional
    public ResumeCreateResponse createResume(Long memberId, ResumeCreateRequest resumeCreateRequest, MultipartFile profileImage) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));

        // 프로필 이미지 업로드 처리
        String imageUrl;
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                imageUrl = s3Service.uploadFile(profileImage, FileDirAndName.ResumeProfileImage);
            } catch (IOException e) {
                throw new BadRequestException(ErrorStatus.RESUME_IMAGE_UPLOAD_FAILED_EXCEPTION.getMessage());
            }
        } else {
            throw new BadRequestException(ErrorStatus.RESUME_PROFILE_IMAGE_REQUIRED_EXCEPTION.getMessage());
        }

        // default 값 설정: 필수값 및 선택값이 있으면 default true
        // 사용자는 3페이지에서 이 값들을 변경하여 이력서에 표시할 항목을 선택할 수 있음
        boolean hasIntroduction = resumeCreateRequest.getIntroduction() != null && !resumeCreateRequest.getIntroduction().trim().isEmpty();
        List<ResumeCreateRequest.ResumeUrlDto> validUrls = new ArrayList<>();
        if (resumeCreateRequest.getUrls() != null) {
            validUrls = resumeCreateRequest.getUrls().stream()
                    .filter(dto -> dto.getUrlTitle() != null && !dto.getUrlTitle().trim().isEmpty()
                            && dto.getUrlLink() != null && !dto.getUrlLink().trim().isEmpty())
                    .toList();
        }
        boolean hasUrls = !validUrls.isEmpty();

        Resume resume = Resume.builder()
                .member(member)
                .resumeName(resumeCreateRequest.getResumeName())
                .template(resumeCreateRequest.getTemplate())
                .introduction(resumeCreateRequest.getIntroduction())
                .profileImageUrl(imageUrl)
                .includeIntroduction(hasIntroduction)
                .includeUrls(hasUrls)
                .includeEducation(false)
                .includeCertificate(false)
                .includeLanguage(false)
                .includeCareer(false)
                .includeAward(false)
                .includeActivity(false)
                .build();

        Resume savedResume = resumeRepository.save(resume);

        // URL 저장
        if (!validUrls.isEmpty()) {
            List<ResumeUrl> urls = validUrls.stream()
                    .map(dto -> ResumeUrl.builder()
                            .resume(savedResume)
                            .urlTitle(dto.getUrlTitle())
                            .urlLink(dto.getUrlLink())
                            .build())
                    .toList();
            resumeUrlRepository.saveAll(urls);
        }

        return ResumeCreateResponse.builder()
                .resumeId(savedResume.getId())
                .resumeName(savedResume.getResumeName())
                .build();
    }

    // 이력서 항목 선택 업데이트
    @Transactional
    public void updateResumeSelections(Long memberId, Long resumeId, ResumeSelectionRequest resumeSelectionRequest) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.RESUME_NOT_FOUND_EXCEPTION.getMessage()));

        // 본인의 이력서인지 확인
        if (!resume.getMember().getId().equals(memberId)) {
            throw new ForbiddenException(ErrorStatus.RESUME_OWNER_FORBIDDEN_EXCEPTION.getMessage());
        }

        resume.updateSelections(
                resumeSelectionRequest.isIncludeIntroduction(),
                resumeSelectionRequest.isIncludeEducation(),
                resumeSelectionRequest.isIncludeCertificate(),
                resumeSelectionRequest.isIncludeLanguage(),
                resumeSelectionRequest.isIncludeCareer(),
                resumeSelectionRequest.isIncludeAward(),
                resumeSelectionRequest.isIncludeActivity(),
                resumeSelectionRequest.isIncludeUrls()
        );
    }

    // 내 이력서 목록 조회
    public Page<ResumeListResponse> getMyResumes(Long memberId, Pageable pageable) {
        Page<Resume> resumes = resumeRepository.findByMemberId(memberId, pageable);

        return resumes.map(resume -> ResumeListResponse.builder()
                .resumeId(resume.getId())
                .resumeName(resume.getResumeName())
                .createdAt(resume.getCreatedAt())
                .updatedAt(resume.getUpdatedAt())
                .build());
    }

    // 이력서 상세 조회
    public ResumeDetailResponse getResume(Long resumeId, Long memberId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.RESUME_NOT_FOUND_EXCEPTION.getMessage()));

        // 본인의 이력서인지 확인
        if (!resume.getMember().getId().equals(memberId)) {
            throw new ForbiddenException(ErrorStatus.RESUME_OWNER_FORBIDDEN_EXCEPTION.getMessage());
        }

        Member member = resume.getMember();

        MemberSpecification memberSpecification = memberSpecificationRepository
                .findByMemberIdId(memberId)
                .orElse(null);

        // 직무 정보 조회
        List<MemberJobRole> memberJobRoles = memberJobRoleRepository.findByMemberId(memberId);
        String jobRole = memberJobRoles.isEmpty() ? null :
                memberJobRoles.stream()
                        .map(mjr -> mjr.getJobRoleEntity().getJobRole())
                        .collect(Collectors.joining(", "));

        // 회원 기본 정보
        ResumeDetailResponse.MemberBasicInfo memberBasicInfo = ResumeDetailResponse.MemberBasicInfo.builder()
                .name(member.getName())
                .jobRole(jobRole)
                .phoneNumber(member.getPhoneNumber())
                .email(member.getEmail())
                .nationality(member.getNationality())
                .visa(member.getVisa())
                .birthday(member.getBirthday() != null ? member.getBirthday().toString() : null)
                .build();

        // URL 목록 (includeUrls가 true일 때만)
        List<ResumeDetailResponse.ResumeUrlDto> urls = new ArrayList<>();
        if (resume.isIncludeUrls()) {
            urls = resumeUrlRepository.findByResumeId(resumeId).stream()
                    .map(url -> ResumeDetailResponse.ResumeUrlDto.builder()
                            .id(url.getId())
                            .urlTitle(url.getUrlTitle())
                            .urlLink(url.getUrlLink())
                            .build())
                    .collect(Collectors.toList());
        }

        // 스펙 정보들 (각 include 플래그가 true이고 memberSpecification이 있을 때만)
        List<ResumeDetailResponse.EducationDto> educations = new ArrayList<>();
        List<ResumeDetailResponse.CertificationDto> certifications = new ArrayList<>();
        List<ResumeDetailResponse.LanguageSkillDto> languageSkills = new ArrayList<>();
        List<ResumeDetailResponse.CareerDto> careers = new ArrayList<>();
        List<ResumeDetailResponse.AwardDto> awards = new ArrayList<>();
        List<ResumeDetailResponse.ExperienceDto> experiences = new ArrayList<>();

        if (memberSpecification != null) {
            Long specId = memberSpecification.getId();

            // 학력
            if (resume.isIncludeEducation()) {
                educations = memberEducationRepository.findAllByMemberSpecificationId(specId).stream()
                        .map(edu -> ResumeDetailResponse.EducationDto.builder()
                                .majors(memberMajorRepository.findAllByMemberEducationId(edu.getId()))
                                .id(edu.getId())
                                .schoolName(edu.getSchoolName())
                                .admissionDate(edu.getAdmissionDate())
                                .graduationDate(edu.getGraduationDate())
                                .earnedScore(edu.getEarnedScore())
                                .maxScore(edu.getMaxScore())
                                .build())
                        .collect(Collectors.toList());
            }

            // 자격증
            if (resume.isIncludeCertificate()) {
                certifications = memberCertificationRepository.findByMemberSpecification(specId).stream()
                        .map(cert -> ResumeDetailResponse.CertificationDto.builder()
                                .id(cert.getId())
                                .certificationName(cert.getCertificationName())
                                .acquiredDate(cert.getAcquiredDate())
                                .documentUrl(cert.getDocumentUrl())
                                .build())
                        .collect(Collectors.toList());
            }

            // 어학능력
            if (resume.isIncludeLanguage()) {
                languageSkills = memberLanguageSkillRepository.findByMemberSpecificationId(specId).stream()
                        .map(lang -> ResumeDetailResponse.LanguageSkillDto.builder()
                                .id(lang.getId())
                                .title(lang.getTitle())
                                .score(lang.getScore())
                                .build())
                        .collect(Collectors.toList());
            }

            // 경력
            if (resume.isIncludeCareer()) {
                careers = memberCareerRepository.findByMemberSpecificationId(specId).stream()
                        .map(career -> ResumeDetailResponse.CareerDto.builder()
                                .id(career.getId())
                                .companyName(career.getCompanyName())
                                .position(career.getPosition())
                                .startDate(career.getStartDate())
                                .endDate(career.getEndDate())
                                .contractType(career.getContractType())
                                .highlight(career.getHighlight())
                                .build())
                        .collect(Collectors.toList());
            }

            // 수상
            if (resume.isIncludeAward()) {
                awards = memberAwardRepository.findByMemberSpecificationId(specId).stream()
                        .map(award -> ResumeDetailResponse.AwardDto.builder()
                                .id(award.getId())
                                .awardName(award.getAwardName())
                                .host(award.getHost())
                                .acquiredDate(award.getAcquiredDate())
                                .description(award.getDescription())
                                .documentUrl(award.getDocumentUrl())
                                .build())
                        .collect(Collectors.toList());
            }

            // 기타활동 (경험)
            if (resume.isIncludeActivity()) {
                experiences = memberExperienceRepository.findByMemberSpecificationId(specId).stream()
                        .map(exp -> ResumeDetailResponse.ExperienceDto.builder()
                                .id(exp.getId())
                                .experience(exp.getExperience())
                                .beforeImprovementRate(exp.getBeforeImprovementRate())
                                .afterImprovementRate(exp.getAfterImprovementRate())
                                .description(exp.getDescription())
                                .startDate(exp.getStartDate())
                                .endDate(exp.getEndDate())
                                .build())
                        .collect(Collectors.toList());
            }
        }

        return ResumeDetailResponse.builder()
                .resumeId(resume.getId())
                .resumeName(resume.getResumeName())
                .template(resume.getTemplate())
                .profileImageUrl(resume.getProfileImageUrl())
                .introduction(resume.isIncludeIntroduction() ? resume.getIntroduction() : null)
                .memberBasicInfo(memberBasicInfo)
                .urls(urls)
                .educations(educations)
                .certifications(certifications)
                .languageSkills(languageSkills)
                .careers(careers)
                .awards(awards)
                .experiences(experiences)
                .includeIntroduction(resume.isIncludeIntroduction())
                .includeEducation(resume.isIncludeEducation())
                .includeCertificate(resume.isIncludeCertificate())
                .includeLanguage(resume.isIncludeLanguage())
                .includeCareer(resume.isIncludeCareer())
                .includeAward(resume.isIncludeAward())
                .includeActivity(resume.isIncludeActivity())
                .includeUrls(resume.isIncludeUrls())
                .build();
    }

    // 이력서 삭제
    @Transactional
    public void deleteResume(Long resumeId, Long memberId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.RESUME_NOT_FOUND_EXCEPTION.getMessage()));

        // 본인의 이력서인지 확인
        if (!resume.getMember().getId().equals(memberId)) {
            throw new ForbiddenException(ErrorStatus.RESUME_OWNER_FORBIDDEN_EXCEPTION.getMessage());
        }

        // 프로필 이미지 삭제
        if (resume.getProfileImageUrl() != null) {
            s3Service.deleteFile(resume.getProfileImageUrl());
        }

        // URL 삭제
        resumeUrlRepository.deleteByResumeId(resumeId);

        // 이력서 삭제
        resumeRepository.delete(resume);
    }
}
