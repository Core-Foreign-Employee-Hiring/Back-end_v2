package com.forwork.backend.api.recruit_review.service;

import com.forwork.backend.api.member.entity.*;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.api.recruit_review.dto.request.RecruitReviewCreateDTO;
import com.forwork.backend.api.recruit_review.dto.response.RecruitReviewDetailResponseDTO;
import com.forwork.backend.api.recruit_review.dto.response.RecruitReviewPreviewResponseDTO;
import com.forwork.backend.api.recruit_review.entity.RecruitReview;
import com.forwork.backend.api.recruit_review.enums.RecruitReviewSortType;
import com.forwork.backend.api.recruit_review.repository.RecruitReviewRepository;
import com.forwork.backend.common.dto.PageResponseDTO;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class RecruitReviewServiceTest {

    @Autowired
    private RecruitReviewService recruitReviewService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

//    @Autowired
    private ReadCountTracker readCountTracker;

//    @Autowired
    private ReadCountFlusher readCountFlusher;

    @Autowired
    private RecruitReviewRepository recruitReviewRepository;

    private Member member;



    @BeforeEach
    void setup(){
        Address address = new Address("zipcode", "address1", "address2");

        Employee employee = new Employee(
                "testUserId",
                "password123!",
                "홍길동",
                "test@example.com",
                "01012345678",
                address,
                "대한민국",      // nationality
                "대학졸업",      // education
                "F-2",          // visa
                LocalDate.now(),
                Gender.MALE,
                true,  // termsOfServiceAgreement
                true,  // isOver15
                true,  // personalInfoAgreement
                false, // adInfoAgreementSmsMms
                false  // adInfoAgreementEmail
        );


        member= memberRepository.save(employee);
    }

    @Test
    void test(){

    }


    /**
     * 게시글 생성 시 tracker 의 newCounts 들어가나 확인.
     */

    @Test
    void 게시글_생성(){
        // given
        RecruitReviewCreateDTO dto = new RecruitReviewCreateDTO("title", "content", "region1", "region2", JobCategory.CONSTRUCTION);


        // when
        Long id = recruitReviewService.createRecruitReview(member.getId(), dto);


        // then

        Map<Long, Integer> readCounts = readCountTracker.getReadCounts(List.of(id));
        Integer i = readCounts.get(id);

        assertEquals(0, i);
    }

    /**
     * 게시글 조회 시 tracker 에 조회수 업데이트와 유저에게 보여줄 조회수 확인.
     * 총 5번 조회헀으므로 둘 다 5인지 테스트
     * flush 전이므로 DB 에 있는 조회수는 0이어야 함.
     */

    @Test
    void 게시글_조회(){
        // given
        RecruitReviewCreateDTO dto = new RecruitReviewCreateDTO("title", "content", "region1", "region2", JobCategory.CONSTRUCTION);
        Long id = recruitReviewService.createRecruitReview(member.getId(), dto);

        // when

        int targetReadCount=5;

        for(int i = 0; i < targetReadCount-1; i++){
            recruitReviewService.getRecruitReview(member.getId(), id);
        }

        entityManager.flush();
        entityManager.clear();

        RecruitReviewDetailResponseDTO recruitReview = recruitReviewService.getRecruitReview(member.getId(), id);

        // then

        Map<Long, Integer> readCounts = readCountTracker.getReadCounts(List.of(id));
        Integer i = readCounts.get(id);

        entityManager.flush();
        entityManager.clear();

        long readCount = recruitReviewRepository.findById(id).get().getReadCount();

        assertEquals(targetReadCount, i);
        assertEquals(targetReadCount, recruitReview.readCount());
        assertEquals(0, readCount);

    }

    /**
     * 게시글 전체 조회 시 조회수 확인
     * 조회수가 0, 3 그리고 5인 게시글 총 3개
     */

    @Test
    void 게시글_페이징(){
        // given

        RecruitReviewCreateDTO dto = new RecruitReviewCreateDTO("title", "content", "region1", "region2", JobCategory.CONSTRUCTION);
        Long readCount0 = recruitReviewService.createRecruitReview(member.getId(), dto);
        Long readCount3 = recruitReviewService.createRecruitReview(member.getId(), dto);
        Long readCount5 = recruitReviewService.createRecruitReview(member.getId(), dto);

        for(int i=0;i<3;i++){
            recruitReviewService.getRecruitReview(member.getId(), readCount3);
        }

        for(int i=0;i<5;i++){
            recruitReviewService.getRecruitReview(member.getId(), readCount5);
        }

        // when

        PageResponseDTO<RecruitReviewPreviewResponseDTO> recruitPreviews = recruitReviewService.getRecruitPreviews(null, 0, 10, RecruitReviewSortType.LATEST);


        // then
        List<RecruitReviewPreviewResponseDTO> content = recruitPreviews.getContent();

        Map<Long, Long> readCountMap = content.stream()
                .collect(Collectors.toMap(
                        RecruitReviewPreviewResponseDTO::recruitReviewId,
                        RecruitReviewPreviewResponseDTO::readCount
                ));

        assertEquals(0, readCountMap.get(readCount0));
        assertEquals(3, readCountMap.get(readCount3));
        assertEquals(5, readCountMap.get(readCount5));

    }

    /**
     * flush 실행 시 db 반영되나 확인.
     */

    @Test
    void db_flush(){
        // given
        RecruitReviewCreateDTO dto = new RecruitReviewCreateDTO("title", "content", "region1", "region2", JobCategory.CONSTRUCTION);
        Long id = recruitReviewService.createRecruitReview(member.getId(), dto);

        int targetReadCount=5;

        for(int i = 0; i < targetReadCount; i++){
            recruitReviewService.getRecruitReview(member.getId(), id);
        }

        // when

        readCountFlusher.flush();


        entityManager.flush();
        entityManager.clear();


        // then
        RecruitReview recruitReview = recruitReviewRepository.findById(id).get();

        Map<Long, Integer> readCounts = readCountTracker.getReadCounts(List.of(id));
        Integer i = readCounts.get(id);

        assertEquals(targetReadCount, i);
        assertEquals(targetReadCount, recruitReview.getReadCount());
    }

    /**
     * 조회수 변화량 많은 글은 tacker 에 유지되고 적은 글은 삭제됨.
     */

    @Test
    void 조회수_변화량_많은_글은_유지되고_적은_글은_삭제된다() {
        // given
        RecruitReviewCreateDTO dto = new RecruitReviewCreateDTO("title", "content", "region1", "region2", JobCategory.CONSTRUCTION);
        Long hot = recruitReviewService.createRecruitReview(member.getId(), dto);
        Long cold=recruitReviewService.createRecruitReview(member.getId(), dto);

        int readCount=10000;

        for(int i=0;i<readCount;i++){
            recruitReviewService.getRecruitReview(member.getId(), hot);
        }

        // when

        readCountFlusher.flush();

        // then

        Map<Long, Integer> readCounts = readCountTracker.getReadCounts(List.of(hot, cold));

        assertEquals(readCount, readCounts.get(hot));
        assertEquals(-1, readCounts.get(cold));
    }

}