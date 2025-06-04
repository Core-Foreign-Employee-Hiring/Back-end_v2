package com.forwork.backend.api.member.repository;

import com.forwork.backend.api.member.entity.PhoneNumberVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneNumberVerificationRepository extends JpaRepository<PhoneNumberVerification, Long> {
    Optional<PhoneNumberVerification> findByPhoneNumber(String phoneNumber);

    Optional<PhoneNumberVerification> findByCode(String code);
}