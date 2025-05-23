package com.nalvey.the_alvey_bank.service.impl;
import org.springframework.beans.factory.annotation.Autowired;

import com.nalvey.the_alvey_bank.dto.TransactionDto;
import com.nalvey.the_alvey_bank.entity.Transaction;
import com.nalvey.the_alvey_bank.repository.TransactionRepository;
import org.springframework.stereotype.Component;

@Component

public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    
    @Override
    public void saveTransaction(TransactionDto transactionDto){
        Transaction transaction = Transaction.builder()
            .transactionType(transactionDto.getTransactionType())
            .accountNumber(transactionDto.getAccountNumber())
            .amount(transactionDto.getAmount())
            .status("SUCCESS")
            .build();
        transactionRepository.save(transaction);
        System.out.println("Transaction saved successfully");
    }

}
