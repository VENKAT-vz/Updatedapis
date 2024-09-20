package com.example.demo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.NewLoan;

public interface NewLoanRepository extends JpaRepository<NewLoan, Integer>{

	@Query("SELECT l from NewLoan l WHERE status='Pending'")
	List<NewLoan> NotApprovedLoans();

	@Modifying
	@Transactional
	@Query("UPDATE User u " +
	       "SET u.income = :newIncome " +
	       "WHERE u.username = (SELECT a.username " +
	                            "FROM Account a " +
	                            "WHERE a.accountNumber = :accountNumber)")
	void updateUserIncomeByAccountNumber(@Param("newIncome") Double newIncome, @Param("accountNumber") String accountNumber);

	
	
}
