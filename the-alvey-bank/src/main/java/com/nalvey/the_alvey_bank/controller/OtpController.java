package com.nalvey.the_alvey_bank.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nalvey.the_alvey_bank.dto.BankResponse;
import com.nalvey.the_alvey_bank.dto.OtpRequest;
import com.nalvey.the_alvey_bank.dto.OtpValidationRequest;
import com.nalvey.the_alvey_bank.service.impl.OtpService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/otp")
@AllArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("sendOtp")
    public BankResponse sendOtp(@RequestBody OtpRequest otpRequest){
        return otpService.sendOtp(otpRequest);
    }

    @PostMapping("validateOtp")
    public BankResponse validateOtp(@RequestBody OtpValidationRequest otpValidationRequest){
        return otpService.validateOtp(otpValidationRequest);
    }
}
