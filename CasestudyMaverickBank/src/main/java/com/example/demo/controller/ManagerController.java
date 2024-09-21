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
import com.example.demo.domain.Loan;
import com.example.demo.domain.LoanDetailsResponse;
import com.example.demo.domain.NewLoan;
import com.example.demo.domain.User;
import com.example.demo.service.AccountService;
import com.example.demo.service.LoanService;
import com.example.demo.service.NewLoanService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/manager")
@CrossOrigin("http://localhost:60753")
public class ManagerController {
	
	@Autowired
	private AccountService accountService;
	
    @Autowired
    private LoanService loanService;
    
    @Autowired
    private NewLoanService newlservice;
    
    @Autowired
	private UserService userservice;
	
    
	
    @GetMapping(value = "/showRequests")
	public List<ApprovalRequest> showUnapproved() {
	    return userservice.BankMrequests();
	}
    
//    @GetMapping("/unapproved-accounts")
//    public ResponseEntity<List<Map<String, Object>>> getNotApprovedAccounts() {
//        List<Map<String, Object>> notApprovedAccounts = accountService.getNotApprovedAccounts();
//        return ResponseEntity.ok(notApprovedAccounts);
//    }
    
    @PutMapping("/close-bank-account/{requestId}")
    public String closeuserAccount(@PathVariable int requestId) {
    	return accountService.CloseBankAccounts(requestId);
    }
    
    @PutMapping("/approve-account/{requestId}")
    public String approveAccount(@PathVariable int  requestId) {
    	return accountService.approveBankAccounts(requestId);
    }
        
//    @PutMapping("/approve-account/{accountNumber}")
//    public String approveAccount(@PathVariable String accountNumber) {
//    	return accountService.approveAccounts(accountNumber);
//    }
    
    @GetMapping("/show-unapprovedloans")
    public List<Loan> unapprovedLoan(){
    	return loanService.UnapprovedLoans();
    }
    
    @PutMapping("/close-account/{accountNumber}")
    public String closeAccount(@PathVariable String accountNumber) {
    	return accountService.closeAccount(accountNumber);
    }
    
    @GetMapping("/showAccounts")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/searchAccounts/{accountNumber}")
    public Account getAccountByNumber(@PathVariable String accountNumber) {
        return accountService.getAccountByNumber(accountNumber);
    }
    
    @DeleteMapping("/delete/{accountNumber}")
    public String deleteAccount(@PathVariable String accountNumber) {
        boolean isDeleted = accountService.deleteAccount(accountNumber);
        if (isDeleted) {
            return "Account deleted successfully.";
        } else {
            return "Account not found.";
        }
    }
    
    @PostMapping("/searchLoan/{accountnumber}")
    public Loan searchLoan(@PathVariable String accountnumber) {
    	return loanService.searchLoan(accountnumber);
    }
    
    @PostMapping("/approveloan/{loanid}")
    public String updateLoan(@PathVariable int loanid) {
    	return loanService.updateLoan(loanid);
    }
    
    @GetMapping("/accountstmts/{accountnumber}")
    public String genereteAccountStatements(@PathVariable String accountnumber) {
    	return accountService.generateAccountStatement(accountnumber);
    }
    
    @GetMapping("/financialreports/{accountnumber}")
    public String financialReports(@PathVariable String accountnumber) {
    	return accountService.generateFinancialReport(accountnumber);
    }
    
//	  new loan table or class:///
    
	@GetMapping("/show-unapproved-loans")
	public List<NewLoan> unapprovedLoans(){
		return newlservice.showUnapprovedLoans();
	}
	

    @GetMapping("/showeligibility/{accountNumber}")
    public ResponseEntity<EligibilityResponse> showEligibility(@PathVariable String accountNumber) {	        EligibilityResponse eligibility = newlservice.showEligibility(accountNumber);
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
	 
	 @GetMapping("/close-loan-details/{loanId}")
	 public LoanDetailsResponse showDetloan(@PathVariable int loanId) {
		 return newlservice.getLoanDetForApproval(loanId);
	 }
	 
	 @PutMapping("/approve-loan-close/{requestId}")
	 public String approveloanclose(@PathVariable int requestId) {
		 return newlservice.approveLoanClose(requestId);
	 }
	 
	 @PutMapping("/reject-loan-close/{requestId}")
	 public String rejectloanclose(@PathVariable int requestId) {
		 return newlservice.RejectLoanClose(requestId);
	 }
    
    
   
}
