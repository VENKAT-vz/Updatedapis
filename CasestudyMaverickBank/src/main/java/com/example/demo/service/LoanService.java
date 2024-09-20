package com.example.demo.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Loan;
import com.example.demo.repository.LoanRepository;

@Service
public class LoanService {
	
	@Autowired
	private LoanRepository loanRepo;
	
	public String addLoan(Loan loan) {
    	Date currentDate = new Date(System.currentTimeMillis());
    	loan.setDateCreated(currentDate);
    	loan.setStatus("NotApproved");
    	
    	loanRepo.save(loan);
    	return "Loan added successfully.";
	}
	
	public Loan searchLoan(String accountnumber) {
		return loanRepo.findByAccountNumber(accountnumber);
	}
	
	public List<Loan> UnapprovedLoans(){
		return loanRepo.NotApprovedLoans();
	}
	
	public String updateLoan(int loanid) {
	    Loan existingLoan = loanRepo.findById(loanid).orElse(null);
	    if (existingLoan != null) {
	        existingLoan.setStatus("approved");
	        loanRepo.save(existingLoan); 
	        return "Loan updated successfully.";
	    }
	    return "Loan not found.";
	}
	
    public List<Loan> viewLoansByAccountNumber(String accountNumber) {
        return loanRepo.findAllByAccountNumber(accountNumber);
    }
	

}
