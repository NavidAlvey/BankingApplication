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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
public class UserController {
    @Autowired
    UserService userService;
//--------------------------------------------------------------------------------------
    @Operation(
        summary = "Create New User Account",
        description = "Creating a new user and assigning an account ID"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Http Status 201 CREATED"
    )
    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }
//--------------------------------------------------------------------------------------
    @Operation(
        summary = "Balance Inquiry",
        description = "Given an account number, check the user's account balance"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Http Status 200 SUCCESS"
    )
    @GetMapping("balanceInquiry")
    public BankResponse balanceInquiry(@RequestBody InquiryRequest request) {
        return userService.balanceInquiry(request);
    }
//--------------------------------------------------------------------------------------
    @Operation(
        summary = "Name Inquiry",
        description = "Given an account number, get the associated account information"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Http Status 200 SUCCESS"
    )
    @GetMapping("nameInquiry")
    public String nameInquiry(@RequestBody InquiryRequest request) {
        return userService.nameInquiry(request);
    }
//--------------------------------------------------------------------------------------
    @Operation(
        summary = "Credit Account",
        description = "Given a certain amount, credit it towards a specified account number"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Http Status 200 SUCCESS"
    )
    @PostMapping("credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
        return userService.creditAccount(request);
    }
//--------------------------------------------------------------------------------------
    @Operation(
        summary = "Debit Account",
        description = "Remove a certain amount from a specified account number"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Http Status 200 SUCCESS"
    )
    @PostMapping("debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request) {
        return userService.debitAccount(request);
    }
//--------------------------------------------------------------------------------------
    @Operation(
        summary = "Transfer Debit to Credit",
        description = "Remove a certain amount from a specified source account number and credit it towards a destination account number as a transfer transaction"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Http Status 200 SUCCESS"
    )
    @PostMapping("transfer")
    public BankResponse transfer(@RequestBody TransferRequest request) {
        return userService.transfer(request);
    }
}
