package com.example.demo.service;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Account;
import com.example.demo.domain.EligibilityResponse;
import com.example.demo.domain.NewLoan;
import com.example.demo.domain.Transaction;
import com.example.demo.domain.User;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.NewLoanListRepository;
import com.example.demo.repository.NewLoanRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;

@Service
public class NewLoanService {


	
	 @Autowired
	private NewLoanRepository newLoanrepo;

	@Autowired
	private NewLoanListRepository newloanlistrepo;
	
	@Autowired
	private InstallmentsService installmentservice;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	
	@Transactional
	public NewLoan applyloan(NewLoan loan,double income) {
		
		newLoanrepo.updateUserIncomeByAccountNumber(income,loan.getAccountNumber());
		newLoanrepo.save(loan);

		return loan;
	}
	
	public List<NewLoan> showUnapprovedLoans() {
		return newLoanrepo.NotApprovedLoans();
	}
	
  public String approveLoan(int loanId, double sanctionAmount, int period) {
      Optional<NewLoan> optionalLoan = newLoanrepo.findById(loanId);
      
	        if (!optionalLoan.isPresent()) {
	            return "Loan not found with ID: " + loanId;
	        }
	        
	        NewLoan loan = optionalLoan.get();
	        loan.setSanctionAmount(sanctionAmount);
	        loan.setPeriod(period);
	        
	        accountRepository.depositAmount(loan.getAccountNumber(), sanctionAmount);
	        
	        Transaction transaction = new Transaction();
	        transaction.setAccountNumber(loan.getAccountNumber());
	        transaction.setAmount(sanctionAmount);
	        transaction.setTransactionDate(new java.sql.Date(new Date().getTime()));
	        transaction.setTransactionType("Loan rebursement");
	        transaction.setDescription("Loan Amount deposited into account");

	        transactionRepository.save(transaction);
	        
	        String loanName = loan.getLoanName();
	        double interestRate = newloanlistrepo.findByLoanName(loanName);

	         double monthlyInterestRate = interestRate / (12 * 100);

	        double p = sanctionAmount; 
	        double r = monthlyInterestRate; 
	       int n = period*12; 

	        double installmentsPerMonth = p * r * Math.pow(1 + r, n) / (Math.pow(1 + r, n) - 1);
      	    double totalAmount = installmentsPerMonth * n;
	        
	       loan.setInstallmentsPerMonth(installmentsPerMonth);
	        loan.setTotalAmount(totalAmount);
	       loan.setLoanApprovedDate(new java.sql.Date(System.currentTimeMillis())); 
	        loan.setStatus("Approved");

	       newLoanrepo.save(loan);

	       installmentservice.createInstallment(loan.getLoanId(), loan.getLoanApprovedDate(), installmentsPerMonth);

	        return "Loan approved successfully with ID: " + loan.getLoanId();
	    }
  
  
  public EligibilityResponse showEligibility(String accountNumber) {
	  
      Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
      if (!optionalAccount.isPresent()) {
          throw new IllegalArgumentException("Account not found with account number: " + accountNumber);
      }

      Account account = optionalAccount.get();

      User user = userRepository.findByUsername(account.getUsername());
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
  
  public String RejectLoan(int loanId) {
	  Optional<NewLoan> optionalloan = newLoanrepo.findById(loanId);
	  if (!optionalloan.isPresent()) {
          throw new IllegalArgumentException("Loan not found with loanid: " + loanId);
      }
	  
	  NewLoan loan = optionalloan.get();
	  loan.setStatus("Rejected");
	  newLoanrepo.save(loan);
	  return "Loan rejected";
  }
}
  

