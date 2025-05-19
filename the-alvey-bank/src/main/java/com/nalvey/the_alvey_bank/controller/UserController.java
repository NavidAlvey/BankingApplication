package com.nalvey.the_alvey_bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nalvey.the_alvey_bank.dto.BankResponse;
import com.nalvey.the_alvey_bank.dto.CreditDebitRequest;
import com.nalvey.the_alvey_bank.dto.InquiryRequest;
import com.nalvey.the_alvey_bank.dto.TransferRequest;
import com.nalvey.the_alvey_bank.dto.UserRequest;
import com.nalvey.the_alvey_bank.service.impl.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

    @GetMapping("balanceInquiry")
    public BankResponse balanceInquiry(@RequestBody InquiryRequest request) {
        return userService.balanceInquiry(request);
    }

    @GetMapping("nameInquiry")
    public String nameInquiry(@RequestBody InquiryRequest request) {
        return userService.nameInquiry(request);
    }

    @PostMapping("credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
        return userService.creditAccount(request);
    }

    @PostMapping("debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request) {
        return userService.debitAccount(request);
    }

    @PostMapping("transfer")
    public BankResponse transfer(@RequestBody TransferRequest request) {
        return userService.transfer(request);
    }
}
