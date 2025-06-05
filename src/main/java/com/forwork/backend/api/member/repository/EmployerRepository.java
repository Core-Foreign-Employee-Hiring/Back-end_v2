package com.forwork.backend.api.member.repository;

import com.forwork.backend.api.member.entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerRepository extends JpaRepository<Employer, Long> {

}
