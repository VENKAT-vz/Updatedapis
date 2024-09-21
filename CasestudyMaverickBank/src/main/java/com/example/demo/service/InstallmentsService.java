package com.example.demo.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Installments;
import com.example.demo.domain.NewLoan;
import com.example.demo.repository.InstallmentsRepository;
import com.example.demo.repository.NewLoanRepository;

@Service
public class InstallmentsService {
	
	@Autowired
	private InstallmentsRepository installmentrepo;
	
	@Autowired
	private NewLoanRepository newLoanrepo;


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

}



