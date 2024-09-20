package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.domain.NewLoanList;

public interface NewLoanListRepository extends JpaRepository<NewLoanList, Integer>{

	@Query("Select l.interestRate from NewLoanList l where loanName =:loanname")
	double findByLoanName(String loanname);
}