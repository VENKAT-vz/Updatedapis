package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.domain.NewLoanList;

public interface NewLoanListRepository extends JpaRepository<NewLoanList, Integer>{

	@Query("Select l.interestRate from NewLoanList l where loanName =:loanname")
	double findByLoanName(String loanname);

	@Query("Select l from NewLoanList l where loanName=:loanName")
	Optional<NewLoanList> findUsingLoanName(String loanName);
}