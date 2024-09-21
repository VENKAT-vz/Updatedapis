package com.example.demo.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Account;
import com.example.demo.domain.Installments;
import com.example.demo.domain.NewLoan;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.InstallmentsRepository;
import com.example.demo.repository.NewLoanRepository;

@Service
public class InstallmentsService {
	
	@Autowired
	private InstallmentsRepository installmentrepo;
	
	@Autowired
	private NewLoanRepository newLoanrepo;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private AccountRepository accountRepository;

	public void createInstallment(int loanId, Date loanApprovedDate, double installmentAmount) {
        Date firstInstallmentDate = new Date(loanApprovedDate.getTime() + (30L * 24 * 60 * 60 * 1000));

        Installments installment = new Installments();
        installment.setLoanId(loanId);
        
        installment.setInstallmentDate(new java.sql.Date(firstInstallmentDate.getTime()));
        installment.setInstallmentAmount(installmentAmount);
        
        installment.setInstallmentStatus("Pending");

        installmentrepo.save(installment);
    }
	
	public String payInstallment(int loanId, String paymentDate, String paymentType, double paymentAmount, String remarks) {

		  Optional<Installments> optionalInstallment = installmentrepo.findByLoanIdAndInstallmentStatus(loanId, "Pending");

	      if (!optionalInstallment.isPresent()) {
	          return "No pending installments found for loan ID: " + loanId;
	      }

	      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	      LocalDate localDate = LocalDate.parse(paymentDate, formatter);

	      Date PaymentDate = Date.valueOf(localDate);
	      Installments installment = optionalInstallment.get();

	      installment.setPaymentDate(PaymentDate);
	      installment.setPaymentType(paymentType);
	      installment.setPaymentAmount(paymentAmount);
	      installment.setRemarks(remarks);
	      installment.setInstallmentStatus("Paid"); 

	      installmentrepo.save(installment);

	      addRepaymentPoints(loanId, PaymentDate, installment.getInstallmentDate());

	      int res = updateTotalBalance(loanId, paymentAmount);
	      if(res==0) {
	    	  return "Total amount for the loan is paid for this loan id..."+loanId;
	      }
	      else {
	    	  createNextInstallment(loanId);
	      
	    	  return "Installment payment successful for loan ID: " + loanId;
	      }
	  }
	  
	  private int updateTotalBalance(int loanId, double paymentAmount) {
	      Optional<NewLoan> optionalLoan = newLoanrepo.findById(loanId);
	      if (!optionalLoan.isPresent()) {
	          throw new RuntimeException("Loan not found with ID: " + loanId);
	      }

	      NewLoan loan = optionalLoan.get();
	      double newTotalBalance = loan.getTotalAmount() - paymentAmount;
	      
	      loan.setTotalAmount(newTotalBalance);

	      if (loan.getStatus().equals("CanClose") && newTotalBalance <= 0) {
	          loan.setStatus("Closed");
	 	     newLoanrepo.save(loan);
	 	     return 0;
	      }
	     newLoanrepo.save(loan);
 	     return 1;
	  }
	  
	
	 private void createNextInstallment(int loanId) {

		 Optional<Installments> optionalInstallment = installmentrepo.findFirstByLoanIdOrderByPaymentDateDesc(loanId);
	        
		 if (!optionalInstallment.isPresent()) 
	            throw new RuntimeException("No installment found for loan ID: " + loanId);
	        

	        Installments lastInstallment = optionalInstallment.get();
	         Date nextInstallmentDate = new Date(lastInstallment.getInstallmentDate().getTime() + (30L * 24 * 60 * 60 * 1000));

	        Installments newInstallment = new Installments();
	        newInstallment.setLoanId(loanId);
	        
	        newInstallment.setInstallmentDate(nextInstallmentDate);
	        newInstallment.setInstallmentAmount(lastInstallment.getInstallmentAmount());
	        
	      newInstallment.setInstallmentStatus("Pending");

	        installmentrepo.save(newInstallment);
	    }
	 
	 //if the customer pays installments before due date +2 points
	 //if late -2 points, no points for on date payment
	 //if severely late -5 points like more than 30 days
	 public void addRepaymentPoints(int loanId, Date paymentDate, Date installmentDate) {

		 	LocalDate paymentLocalDate = paymentDate.toLocalDate();
		    LocalDate installmentLocalDate = installmentDate.toLocalDate();

		    NewLoan loan = newLoanrepo.findById(loanId).orElseThrow(() -> new RuntimeException("Loan not found"));

		    Period period = Period.between(installmentLocalDate, paymentLocalDate);
		    int daysDifference = period.getDays();
		    
		    int pointsToAdd = 0;
		    
		    if (daysDifference < 0) {
		        pointsToAdd = 2;  
		    }
		    else if (daysDifference == 0) {
		        pointsToAdd = 0;  
		    }
		    else if (daysDifference > 0 && daysDifference < 30) {
		        pointsToAdd = -2;  
		    }
		    else if (daysDifference >= 30) {
		        pointsToAdd = -5;  
		    }

		    loan.setRepaymentPoints(loan.getRepaymentPoints() + pointsToAdd);
		    newLoanrepo.save(loan);
		}
	 
	 
	  
	 //automated mail sender, it will send mail to the 
	 //customer who's due date is tomorrow
	    @Scheduled(cron = "0 30 9 * * ?") 
	    @Transactional
	   public void automateInstallmentReminders() {
	        List<Installments> pendingInstallments = installmentrepo.findByInstallmentStatus("Pending");

	  for (Installments installment : pendingInstallments) {
		  LocalDate installmentDate = installment.getInstallmentDate().toLocalDate();
	      LocalDate reminderDate = installmentDate.minusDays(1);

	    if (LocalDate.now().equals(reminderDate)) {
	        	   
	       Optional<NewLoan> optionalLoan = newLoanrepo.findById(installment.getLoanId());
	            
	      if (optionalLoan.isPresent()) {
	            	
            NewLoan loan = optionalLoan.get();
	                    
	         Optional<Account> optionalAccount = accountRepository.findByAccountNumber(loan.getAccountNumber());
	         if (optionalAccount.isPresent()) {
	             Account account = optionalAccount.get();
	             String customerEmail = account.getEmailid();
	             String subject = "Reminder: Your Installment is Due Tomorrow";
	             String body = "Dear customer, your next installment of " + installment.getInstallmentAmount() + 
	                                      " is due on " + installmentDate + ". Please make the payment on time.";

	             emailService.sendInstallmentReminder(customerEmail, subject, body);
	        }
	   
	      }        
	    }
	   
	   }
	 }


}



