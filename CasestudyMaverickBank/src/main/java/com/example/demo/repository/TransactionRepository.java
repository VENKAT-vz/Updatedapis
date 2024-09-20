package com.example.demo.repository;

import java.util.List;

import org.jboss.logging.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.accountNumber = :accountNumber AND t.transactionType = 'deposit' OR t.transactionType = 'transfer-in'")
    Double sumDeposits( String accountNumber);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.accountNumber = :accountNumber AND t.transactionType = 'withdrawal' OR t.transactionType = 'transfer-out'")
    Double sumWithdrawals(String accountNumber);
    
    List<Transaction> findByAccountNumberOrderByTransactionDateDesc(String accountNumber);


	
}