package com.nalvey.the_alvey_bank.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private String transactionType;
    private String accountNumber;
    private String status;
    private BigDecimal amount;
}
