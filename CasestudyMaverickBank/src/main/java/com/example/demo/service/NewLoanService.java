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
	private NewLoanListRepository newloanlistrepo;
	
	@Autowired
	private InstallmentsService installmentservice;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	 @Autowired
	private NewLoanListRepository newLoanListRepository;
	 
	 @Autowired
		private ApprovalRequestRepository approvalRequestRepository;
	
	@Transactional
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
	        transaction.setTransactionDate(new Date(System.currentTimeMillis()));
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
  
  public String RejectLoan(int requestId) {
	  
	  Optional<ApprovalRequest> optionalapprovalRequest = approvalRequestRepository.findById(requestId);
		 ApprovalRequest approvalRequest=optionalapprovalRequest.get();
		 
		 String loanid=approvalRequest.getActionNeededOn();
	  Optional<NewLoan> optionalloan = newLoanrepo.findById(Integer.valueOf(loanid));
	  if (!optionalloan.isPresent()) {
          throw new IllegalArgumentException("Loan not found with loanid: " + loanid);
      }
	  
	  NewLoan loan = optionalloan.get();
	  loan.setStatus("Rejected");
	  newLoanrepo.save(loan);
	  
		 approvalRequest.setStatus("Rejected");
		 approvalRequest.setRemarks("We need accurate details for cross verifying your details. "
		 		+ "For now it is not enough. Try again with accurate details");
		 approvalRequestRepository.save(approvalRequest);
	  return "Loan rejected";
  }
  
  public String LoanClose(int loanId) {
	  
	    ApprovalRequest approvalRequest = new ApprovalRequest();
	    approvalRequest.setToWhom("BankManager");
	    approvalRequest.setRequirement("LoanClosure"); 
	    approvalRequest.setActionNeededOn( Integer.toString(loanId)); 
	    approvalRequest.setStatus("Pending"); 
	    approvalRequest.setCreatedAt(new Date(System.currentTimeMillis()));
	    approvalRequestRepository.save(approvalRequest);
	    
	    return "Loan Closure is requested to the BankManager...";
  }
  

  
  public LoanDetailsResponse getLoanDetForApproval(int loanId) {

	  Optional<NewLoan> optionalLoan = newLoanrepo.findById(loanId);
	    if (!optionalLoan.isPresent()) {
	        throw new RuntimeException("Loan with ID " + loanId + " not found.");
	    }

	    NewLoan loan = optionalLoan.get();

	    Optional<NewLoanList> optionalLoanList = newLoanListRepository.findUsingLoanName(loan.getLoanName());
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
  
  public String RejectLoanClose(int requestId) {
			 Optional<ApprovalRequest> optionalapprovalRequest = approvalRequestRepository.findById(requestId);
			 ApprovalRequest approvalRequest=optionalapprovalRequest.get();
			 approvalRequest.setStatus("Rejected");
			 approvalRequest.setRemarks("The reason for rejection is your repayments points "
			 		+ "and minimum tenure of early repayments have not met the condition..");
			 approvalRequestRepository.save(approvalRequest);
			 
			 return "The loan account is rejected...";
			 
  }
  
	 public String approveLoanClose(int requestId) {
		 Optional<ApprovalRequest> optionalapprovalRequest = approvalRequestRepository.findById(requestId);
		 ApprovalRequest approvalRequest=optionalapprovalRequest.get();
		 
		 String loanid=approvalRequest.getActionNeededOn();
		 Optional<NewLoan> optionalloan=newLoanrepo.findById(Integer.valueOf(loanid));
		 
		 NewLoan loan=optionalloan.get();
		 loan.setStatus("CanClose");
		 newLoanrepo.save(loan);
		 
		 approvalRequest.setStatus("Approved");
		 approvalRequest.setRemarks("You can now pay the entire loan early and close the loan. The loan closing is approved");
		 approvalRequestRepository.save(approvalRequest);
		 
		 return "The loan closing is approved successfully....";
		 
	      
}

}
  

