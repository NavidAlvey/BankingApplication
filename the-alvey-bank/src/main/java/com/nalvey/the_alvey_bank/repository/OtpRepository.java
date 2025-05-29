package com.nalvey.the_alvey_bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nalvey.the_alvey_bank.entity.Otp;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Otp findByEmail(String email);
}
