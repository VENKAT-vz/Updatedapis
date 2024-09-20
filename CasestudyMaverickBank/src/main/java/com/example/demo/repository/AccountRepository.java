package com.example.demo.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

	List<Account> findByUsername(String username);
	
	Optional<Account> findByAccountNumber(String accountNumber);
	
    @Query("SELECT a FROM Account a JOIN a.user u WHERE u.contactNumber = :phoneNumber")
    Account findByUserContactNumber(String phoneNumber);

    @Query("SELECT a.balance FROM Account a WHERE a.accountNumber = :accountNumber")
    double findBalanceByAccountNumber(String accountNumber);

    @Query("SELECT a.balance FROM Account a WHERE a.user.username = :username")
    double findBalanceByUsername(String username);
    
    @Query("SELECT MAX(a.accountNumber) FROM Account a")
    String findMaxAccountNumber();
    
    @Query("SELECT new map(a.accountNumber as accountNumber, a.accountType as accountType, a.balance as balance, a.branchName as branchName, a.ifscCode as ifscCode, " +
            "a.dateCreated as dateCreated, a.status as status, u.username as username, u.emailid as emailid, u.aadharNum as aadharNum, u.panNum as panNum) " +
            "FROM Account a JOIN a.user u WHERE a.status = 'NotApproved'")
     List<Map<String, Object>> findNotApprovedAccounts();
    
    
    @Transactional
    @Modifying
    @Query("UPDATE Account a SET a.status = 'active' WHERE a.accountNumber = :accountNumber")
    int approveAccount(@Param("accountNumber") String accountNumber);
    
    @Transactional
    @Modifying
    @Query("UPDATE Account a SET a.status = 'closed' WHERE a.accountNumber = :accountNumber")
    int closeAccount(@Param("accountNumber") String accountNumber);
    
    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.accountNumber = :accountNumber")
    void depositAmount(String accountNumber, double amount);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = a.balance - :amount WHERE a.accountNumber = :accountNumber")
    void withdrawAmount(String accountNumber, double amount);
}
