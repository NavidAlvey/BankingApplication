package com.nalvey.the_alvey_bank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)

public class BankResponse {
    private String responseCode;
    private String responseMessage;
    private AccountInfo accountInfo;
    private OtpResponse otpResponse;
}
