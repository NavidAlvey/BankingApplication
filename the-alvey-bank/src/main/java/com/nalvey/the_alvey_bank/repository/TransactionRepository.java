package com.nalvey.the_alvey_bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nalvey.the_alvey_bank.entity.Transaction;


public interface TransactionRepository extends JpaRepository<Transaction, String>{
    
}
