package com.example.demo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Integer>{

	Loan findByAccountNumber(String accountnumber);
	
	List<Loan> findAllByAccountNumber(String accountNumber);
	
	@Query("SELECT l from Loan l WHERE status='NotApproved'")
	List<Loan> NotApprovedLoans();
	
//	@Query("UPDATE User u join account a on u.username=a.username join Loan "
//			+ "on l.accountnumber=a.accountnumber set u.income =join ")
//	

}
