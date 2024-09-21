package com.example.demo.service;


import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Account;
import com.example.demo.domain.ApprovalRequest;
import com.example.demo.domain.EligibilityResponse;
import com.example.demo.domain.LoanDetailsResponse;
import com.example.demo.domain.Login;
import com.example.demo.domain.NewLoan;
import com.example.demo.domain.NewLoanList;
import com.example.demo.domain.Transaction;
import com.example.demo.domain.User;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.ApprovalRequestRepository;
import com.example.demo.repository.NewLoanListRepository;
import com.example.demo.repository.NewLoanRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;

@Service
public class NewLoanService {

	 	@Autowired
	 	private NewLoanRepository newLoanrepo;
	 
	 	@Autowired
		private ApprovalRequestRepository approvalRequestRepository;
	
		@Autowired
		private NewLoanListRepository newloanlistrepo;
		
		@Autowired
		private TransactionRepository transactionRepository;
		
		@Autowired
		private UserRepository userRepo;
		
		@Autowired
		private AccountRepository accountRepository;
	 
		
	 public NewLoan searchloan(int loanid) {
		 Optional<NewLoan> optionalloan = newLoanrepo.findById(loanid);
		 return optionalloan.get();
	 }
	 
	 public List<NewLoan> showAll(){
		 return newLoanrepo.findAll();
	 }
	 
	 public String deleteLoan(int loanid) {
		 if(newLoanrepo.existsById(loanid)) {
			 newLoanrepo.deleteById(loanid);
			 return "Loan deleted";
		 }
		 return "Loan not deleted";
	 }
	 
	public NewLoan applyloan(NewLoan loan,double income) {
		
		newLoanrepo.updateUserIncomeByAccountNumber(income,loan.getAccountNumber());
		newLoanrepo.save(loan);
		
		ApprovalRequest approvalRequest = new ApprovalRequest();
	    approvalRequest.setToWhom("BankManager");
	    approvalRequest.setRequirement("LoanApproval"); 
	    approvalRequest.setActionNeededOn( Integer.toString(loan.getLoanId())); 
	    approvalRequest.setStatus("Pending"); 
	    approvalRequest.setCreatedAt(new Date(System.currentTimeMillis()));
	    approvalRequestRepository.save(approvalRequest);
	    
		return loan;
	}
	
	  
	  public EligibilityResponse showEligibility(int requestId) {
		  
		  Optional<ApprovalRequest> optionalapprovalRequest = approvalRequestRepository.findById(requestId);
			 ApprovalRequest approvalRequest=optionalapprovalRequest.get();
			 
		  String loanid=approvalRequest.getActionNeededOn();
		  Optional<NewLoan> optionalloan = newLoanrepo.findById(Integer.valueOf(loanid));
		  if (!optionalloan.isPresent()) {
	          throw new IllegalArgumentException("Loan not found with loanid: " + loanid);
	      }
		  
		  NewLoan loan = optionalloan.get();
		  String accountNumber=loan.getAccountNumber();
	      Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
	      if (!optionalAccount.isPresent()) {
	          throw new IllegalArgumentException("Account not found with account number: " + accountNumber);
	      }

	      Account account = optionalAccount.get();

	      User user = userRepo.findByUsername(account.getUsername());
	      if (user == null) {
	          throw new IllegalArgumentException("User not found for account number: " + accountNumber);
	      }

	      Double income = user.getIncome();

	      Double inboundAmount = transactionRepository.sumDeposits(accountNumber);
	      Double outboundAmount = transactionRepository.sumWithdrawals(accountNumber);

	      String verdict;
	      if (inboundAmount > outboundAmount) {
	          verdict = "Inbound is greater";
	      } else if (outboundAmount > inboundAmount) {
	          verdict = "Outbound is greater";
	      } else {
	          verdict = "Inbound and outbound are equal";
	      }

	      EligibilityResponse response = new EligibilityResponse();
	      response.setAccountNumber(accountNumber);
	      response.setUsername(user.getUsername());
	      response.setIncome(income);
	      response.setInboundAmount(inboundAmount);
	      response.setOutboundAmount(outboundAmount);
	      response.setVerdict(verdict);

	      return response;
	  }
	  
	 
	  public LoanDetailsResponse getLoanDetForApproval(int loanId) {

		  Optional<NewLoan> optionalLoan = newLoanrepo.findById(loanId);
		    if (!optionalLoan.isPresent()) {
		        throw new RuntimeException("Loan with ID " + loanId + " not found.");
		    }

		    NewLoan loan = optionalLoan.get();

		    Optional<NewLoanList> optionalLoanList = newloanlistrepo.findUsingLoanName(loan.getLoanName());
		    if (!optionalLoanList.isPresent()) {
		        throw new RuntimeException("Loan type " + loan.getLoanName() + " not found.");
		    }

		    NewLoanList loanList = optionalLoanList.get();

		    double outstandingBalance = loan.getTotalAmount();

		    LocalDate loanApprovedDate = loan.getLoanApprovedDate().toLocalDate();
		    LocalDate today = LocalDate.now();

		    Period period = Period.between(loanApprovedDate, today);

		    int monthsPassed = period.getYears() * 12 + period.getMonths();

		    LoanDetailsResponse loanDetails = new LoanDetailsResponse();
		    loanDetails.setLoanId(loan.getLoanId());
		    loanDetails.setLoanName(loan.getLoanName());
		    loanDetails.setRepaymentPoints(loan.getRepaymentPoints());
		    loanDetails.setMinTenureMonths(loanList.getMinTenure());
		    loanDetails.setOutstandingBalance(outstandingBalance);

		    if (monthsPassed >= loanList.getMinTenure() && loan.getRepaymentPoints() > 15) {
		    	loanDetails.setVerdict("Loan is ready to close.");
		    } else if (monthsPassed < loanList.getMinTenure()) {
		    	loanDetails.setVerdict("Loan cannot be closed. Minimum tenure of " + loanList.getMinTenure() + " months not met.");
		    } else if (loan.getRepaymentPoints() <= 15) {
		    	loanDetails.setVerdict("Loan cannot be closed. Repayment points must be greater than 15.");
		    }

		    return loanDetails;
		}
     
}

  

