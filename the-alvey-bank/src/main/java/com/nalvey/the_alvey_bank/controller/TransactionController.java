package com.nalvey.the_alvey_bank.controller;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.nalvey.the_alvey_bank.entity.Transaction;
import com.nalvey.the_alvey_bank.service.impl.BankStatement;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/bankStatement")
@AllArgsConstructor
public class TransactionController {
    private BankStatement bankStatement;

    @GetMapping
    public List<Transaction> generateBankStatement(@RequestParam String accountNumber, @RequestParam String startDate, @RequestParam String endDate) throws FileNotFoundException, DocumentException{
        return bankStatement.generateStatement(accountNumber, startDate, endDate);
    }
}
