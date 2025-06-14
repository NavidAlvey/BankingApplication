package com.nalvey.the_alvey_bank.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.nalvey.the_alvey_bank.dto.BankResponse;
import com.nalvey.the_alvey_bank.dto.EmailDetails;
import com.nalvey.the_alvey_bank.dto.OtpRequest;
import com.nalvey.the_alvey_bank.dto.OtpResponse;
import com.nalvey.the_alvey_bank.dto.OtpValidationRequest;
import com.nalvey.the_alvey_bank.entity.Otp;
import com.nalvey.the_alvey_bank.repository.OtpRepository;
import com.nalvey.the_alvey_bank.utils.AccountUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
// OTP = One Time Password
@Data
@Builder
@Service
@AllArgsConstructor
@Slf4j
public class OtpService {
    private final OtpRepository otpRepository;
    private final EmailService emailService;

    public BankResponse sendOtp(OtpRequest otpRequest){
        // generate otp
        // send otp
        // save otp
        
        String otp = AccountUtils.generateOtp();
        log.info("Otp: {}", otp);
        otpRepository.save(Otp.builder()
            .email(otpRequest.getEmail())
            .otp(otp)
            .expiresAt(LocalDateTime.now().plusMinutes(5))
            .build());
       
        emailService.sendEmailAlert(EmailDetails.builder()
            .subject("SECURITY CODE")
            .recipient(otpRequest.getEmail())
            .messageBody("Please use the following security code OTP for the account " + otpRequest.getEmail() +
             "\nSecurity Code OTP: " + otp + "\nThis security code will expire in 5 minutes. \n\nIf you don't recognize the account "
             + otpRequest.getEmail() + ", please ignore this message. \n\nThanks, \nThe Alvey Bank")
            .build());

        return BankResponse.builder()
            .responseCode(AccountUtils.OTP_SENT_CODE)
            .responseMessage(AccountUtils.OTP_SENT_MESSAGE)
            .build();
    }

    // determine if the user actually sent an OTP (if user didn't send, return error message)
    // determine if OTP hasn't expired
    // determine if OTP is correct

    public BankResponse validateOtp(OtpValidationRequest OtpValidationRequest){
        Otp otp = otpRepository.findByEmail(OtpValidationRequest.getEmail());
        log.info("Email: {}", OtpValidationRequest.getEmail());
        if (otp == null) {
            return BankResponse.builder()
                .responseCode(AccountUtils.OTP_NOT_SENT_CODE)
                .responseMessage(AccountUtils.OTP_NOT_SENT_MESSAGE)
                .build();
        }
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())){
            return BankResponse.builder()
                .responseCode(AccountUtils.OTP_EXPIRED_CODE)
                .responseMessage(AccountUtils.OTP_EXPIRED_MESSAGE)
                .build();
        }
        if (!otp.getOtp().equals(OtpValidationRequest.getOtp())){
            return BankResponse.builder()
                .responseCode(AccountUtils.OTP_INVALID_CODE)
                .responseMessage(AccountUtils.OTP_INVALID_MESSAGE)
                .build();
        }
        return BankResponse.builder()
            .responseCode(AccountUtils.OTP_VALID_CODE)
            .responseMessage(AccountUtils.OTP_VALID_MESSAGE)
            .otpResponse(OtpResponse.builder()
                .isOtpValid(true)
                .build())
            .build();
    }
}
