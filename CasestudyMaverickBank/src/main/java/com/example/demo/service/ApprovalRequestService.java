package com.example.demo.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

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
import com.example.demo.repository.LoginRepository;
import com.example.demo.repository.NewLoanListRepository;
import com.example.demo.repository.NewLoanRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;

@Service
public class ApprovalRequestService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ApprovalRequestRepository approvalRequestRepository;
	
    @Autowired
    private AccountRepository accountRepository;

	@Autowired
	private LoginRepository lrepo;
	
	 @Autowired
	private NewLoanRepository newLoanrepo;
	 
	 @Autowired
   	 private InstallmentsService installmentservice;
	 
	@Autowired
	private TransactionRepository transactionRepository;
		
	@Autowired
	private NewLoanListRepository newloanlistrepo;


	
	   //admin
		 public String approveUserAccounts(int requestId) {
			 Optional<ApprovalRequest> optionalapprovalRequest = approvalRequestRepository.findById(requestId);
			 ApprovalRequest approvalRequest=optionalapprovalRequest.get();
			 
			 String username=approvalRequest.getActionNeededOn();
			 User user=userRepo.findByUsername(username);
			 user.setStatus("active");
			 userRepo.save(user);
			 
			 Login login = lrepo.findByUsername(username);
			 login.setStatus("active");
			 lrepo.save(login);
			 
			 approvalRequest.setStatus("Approved");
			 approvalRequestRepository.save(approvalRequest);
			 
			 return "The user account is set to active...";
			 
		      
		 }
		 
		    //admin
		 public String RejectUserAccount(int requestId) {
			 
				 Optional<ApprovalRequest> optionalapprovalRequest = approvalRequestRepository.findById(requestId);
				 ApprovalRequest approvalRequest=optionalapprovalRequest.get();
				 
				 String username=approvalRequest.getActionNeededOn();
				 User user=userRepo.findByUsername(username);
				 user.setStatus("rejected");
				 userRepo.save(user);
				 
				 Login login = lrepo.findByUsername(username);
				 
				 lrepo.delete(login);
				 
				 approvalRequest.setStatus("Rejected");
				 approvalRequest.setRemarks("The user details are not satisfactory to create an user account");
				 approvalRequestRepository.save(approvalRequest);
				 
				 return "The user account is rejected...";
				 
			      
		}
		//admin
		 public String CloseUserAccounts(int requestId) {
			 Optional<ApprovalRequest> optionalapprovalRequest = approvalRequestRepository.findById(requestId);
			 ApprovalRequest approvalRequest=optionalapprovalRequest.get();
			 approvalRequest.setStatus("Approved");

			 String username=approvalRequest.getActionNeededOn();
			 User user=userRepo.findByUsername(username);
			 user.setStatus("closed");
			 
			 return "The user account is closed...";
			 
		      
		 }
	    //customer
		 public String closeUserAccountsRequest(String username) {
			 ApprovalRequest approvalRequest = new ApprovalRequest();
			    approvalRequest.setToWhom("Admin");
			    approvalRequest.setRequirement("User Account Closure "); 
			    approvalRequest.setActionNeededOn(username); 
			    approvalRequest.setStatus("Pending"); 
			    approvalRequest.setCreatedAt(new Date(System.currentTimeMillis()));
			    approvalRequestRepository.save(approvalRequest); 
			    return"Closing of user account requested to admin..";
	}
	    
	    
	    
		 //admin
		 public List<ApprovalRequest> requests(){
			 return approvalRequestRepository.showAllrequestsAdmin();
		 }
		 
		 //bankmanager
		 public List<ApprovalRequest> BankMrequests(){
			 return approvalRequestRepository.showAllrequestsBankManager();
		 }
		 
		 
		 ////
		 
			//bankmanager
		 public String CloseBankAccounts(int requestId) {
			 Optional<ApprovalRequest> optionalapprovalRequest = approvalRequestRepository.findById(requestId);
			 ApprovalRequest approvalRequest=optionalapprovalRequest.get();
			 
			 
			 String accno=approvalRequest.getActionNeededOn();
			 Optional<Account> optionalaccount=accountRepository.findByAccountNumber(accno);
			 Account account= optionalaccount.get();
			 account.setStatus("closed");
			 accountRepository.save(account);
			 
			 approvalRequest.setStatus("Approved");

			 approvalRequestRepository.save(approvalRequest);
			 
			 return "The Bank account is closed...";
			 
		      
		 }
		 
		 public String RejectCloseBankAccounts(int requestId) {
			 Optional<ApprovalRequest> optionalapprovalRequest = approvalRequestRepository.findById(requestId);
			 ApprovalRequest approvalRequest=optionalapprovalRequest.get();
			 
			 approvalRequest.setStatus("Rejected");
			 approvalRequest.setRemarks("Can't close account because you have pending payments..");

			 approvalRequestRepository.save(approvalRequest);
			 
			 return "The Bank account closing rejected.";
			 
		      
		 }
	   //customer
		 public String closeBankAccountsRequest(String accountNumber) {
			 ApprovalRequest approvalRequest = new ApprovalRequest();
			    approvalRequest.setToWhom("BankManager");
			    approvalRequest.setRequirement("Bank Account Closure "); 
			    approvalRequest.setActionNeededOn(accountNumber); 
			    approvalRequest.setStatus("Pending"); 
			    approvalRequest.setCreatedAt(new Date(System.currentTimeMillis()));
			    approvalRequestRepository.save(approvalRequest); 
			    
			    return"Closing of Bank account requested to Bank Manager..";
	}
		 //bankmanager
		 public String approveBankAccounts(int requestId) {
			 Optional<ApprovalRequest> optionalapprovalRequest = approvalRequestRepository.findById(requestId);
			 ApprovalRequest approvalRequest=optionalapprovalRequest.get();
			
			 
			 String username=approvalRequest.getActionNeededOn();
			 Optional<Account> optionalaccount=accountRepository.findByAccountNumber(username);
			 Account account=optionalaccount.get();
			 account.setStatus("active");
			 accountRepository.save(account);
			 
			 approvalRequest.setStatus("Approved");
			 approvalRequestRepository.save(approvalRequest);
			 
			 return "The bank account is set to active...";
			 
		      
	}
		 //bankmanager
		 public String rejectBankAccount(int requestId) {
			 Optional<ApprovalRequest> optionalapprovalRequest = approvalRequestRepository.findById(requestId);
			 ApprovalRequest approvalRequest=optionalapprovalRequest.get();
			
			 
			 String username=approvalRequest.getActionNeededOn();
			 Optional<Account> optionalaccount=accountRepository.findByAccountNumber(username);
			 Account account=optionalaccount.get();
			 account.setStatus("Rejected");
			 accountRepository.save(account);
			 
			 approvalRequest.setStatus("Rejected");
			 approvalRequest.setRemarks("Based on the details given, we can't open an account for you");
			 approvalRequestRepository.save(approvalRequest);
			 
			 return "The bank account is rejected...";
		 }
			 
	///
			 
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
	  
	
}
