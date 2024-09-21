package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Account;
import com.example.demo.domain.ApprovalRequest;
import com.example.demo.domain.EligibilityResponse;
import com.example.demo.domain.LoanDetailsResponse;
import com.example.demo.domain.NewLoan;
import com.example.demo.service.AccountService;
import com.example.demo.service.NewLoanService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/bankmanager")
@CrossOrigin("http://localhost:4200")
public class BankManagerController {
	
	@Autowired
	private AccountService accountService;
	
    @Autowired
    private NewLoanService newlservice;
    
    @Autowired
	private UserService userservice;
	
    //shows every request made to BankManager regarding
    //->Bank account and Loan 
    @GetMapping(value = "/showRequests")
	public List<ApprovalRequest> showUnapproved() {
	    return userservice.BankMrequests();
	}
    
    //Approval,Rejection and Closing of BankAccounts
    @PutMapping("/approve-bank-account/{requestId}")
    public String approveAccount(@PathVariable int  requestId) {
    	return accountService.approveBankAccounts(requestId);
    }
    
    @PutMapping("/reject-bank-account/{requestId}")
    public String rejectAccount(@PathVariable int  requestId) {
    	return accountService.rejectBankAccount(requestId);
    }
    
    @PutMapping("/close-bank-account/{requestId}")
    public String closeuserAccount(@PathVariable int requestId) {
    	return accountService.CloseBankAccounts(requestId);
    }
    
    @PutMapping("/reject-close-bank-account/{requestId}")
    public String rejectClosing(@PathVariable int requestId) {
    	return accountService.RejectCloseBankAccounts(requestId);
    }
    
    //Checks loan eligibility and decides to approve or reject loans
    @GetMapping("/showeligibility/{requestId}")
    public ResponseEntity<EligibilityResponse> showEligibility(@PathVariable int requestId) {	        
    	EligibilityResponse eligibility = newlservice.showEligibility(requestId);
       return ResponseEntity.ok(eligibility);
    }

	 @PutMapping("/approve-loan")
	 public String approveloan(@RequestParam int loanId, @RequestParam double sanctionAmount, 
			 @RequestParam int period) {
		 return newlservice.approveLoan(loanId, sanctionAmount, period);
	 }
	 
	 @PutMapping("/reject-loan/{requestId}")
	 public String approveloan(@PathVariable int requestId) {
		 return newlservice.RejectLoan(requestId);
	 }
	 
	 //Checks condition for early loan closing
	 //Based on that, decides to approve or reject the loan closing 
	 @GetMapping("/close-loan-details/{loanId}")
	 public LoanDetailsResponse showDetloan(@PathVariable int loanId) {
		 return newlservice.getLoanDetForApproval(loanId);
	 }
	 
	 @PutMapping("/approve-loan-closing/{requestId}")
	 public String approveloanclose(@PathVariable int requestId) {
		 return newlservice.approveLoanClose(requestId);
	 }
	 
	 @PutMapping("/reject-loan-closing/{requestId}")
	 public String rejectloanclose(@PathVariable int requestId) {
		 return newlservice.RejectLoanClose(requestId);
	 }

	 //Account statements and Financial report of the customer
	 @GetMapping("/accountstmts/{accountnumber}")
	 public String accountStatements(@PathVariable String accountnumber) {
	      return accountService.generateAccountStatement(accountnumber);
	   }
	 
	 @GetMapping("/financialreports/{accountnumber}")
     public String financialReports(@PathVariable String accountnumber) {
	      return accountService.generateFinancialReport(accountnumber);
	   }
	 
	 
	 //standard methods
    @GetMapping("/showAccounts")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }
    
    @GetMapping("/showLoans")
    public List<NewLoan> getAllLoans() {
        return newlservice.showAll();
    }

    @GetMapping("/searchAccounts/{accountNumber}")
    public Account getAccountByNumber(@PathVariable String accountNumber) {
        return accountService.getAccountByNumber(accountNumber);
    }
    
    @GetMapping("/searchloans/{loanId}")
    public NewLoan getLoans(@PathVariable int loanId) {
        return newlservice.searchloan(loanId);
    }
    
    @DeleteMapping("/deleteAccount/{accountNumber}")
    public String deleteAccount(@PathVariable String accountNumber) {
    	 return accountService.deleteAccount(accountNumber);
    }
    
    @DeleteMapping("/deleteLoan/{loanId}")
    public String deleteAccount(@PathVariable int loanId) {
    	return newlservice.deleteLoan(loanId);
    }
    
    
   
}
