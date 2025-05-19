package com.nalvey.the_alvey_bank.service.impl;

import com.nalvey.the_alvey_bank.dto.BankResponse;
import com.nalvey.the_alvey_bank.dto.CreditDebitRequest;
import com.nalvey.the_alvey_bank.dto.InquiryRequest;
import com.nalvey.the_alvey_bank.dto.TransferRequest;
import com.nalvey.the_alvey_bank.dto.UserRequest;

// createAccount() return type is BankResponse which is a file from dto
// takes in parameters from dto's UserRequest.java and returns the BankResponse
public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceInquiry(InquiryRequest request);
    String nameInquiry(InquiryRequest request);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
    BankResponse transfer(TransferRequest request);
}
 