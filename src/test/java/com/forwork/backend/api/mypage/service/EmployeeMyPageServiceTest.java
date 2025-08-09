package com.forwork.backend.api.mypage.service;

import com.forwork.backend.api.mypage.dto.response.PurchasedArchivesPreviewResponseDTO;
import com.forwork.backend.common.dto.PageResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EmployeeMyPageServiceTest {


    @Autowired
    EmployeeMyPageService employeeMyPageService;


    @Test
    void test(){
        Long memberId=1L;
        Integer page=0, size=10;

        PageResponseDTO<PurchasedArchivesPreviewResponseDTO> purchasedArchives = employeeMyPageService.getPurchasedArchives(memberId, page, size);


        System.out.println(purchasedArchives);
    }

}