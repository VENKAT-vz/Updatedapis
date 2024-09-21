package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Account;
import com.example.demo.domain.ApprovalRequest;

import com.example.demo.domain.Transaction;
import com.example.demo.domain.User;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.ApprovalRequestRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TransactionRepository transRepository;
    
    @Autowired
    private ApprovalRequestRepository approvalRequestRepository;
    

    public Account addAccount(Account account, String aadhaarNumber, String panNumber,Double income) throws ClassNotFoundException, SQLException {
    	account.setAccountNumber(generateAccountNo());
    	Date currentDate = new Date(System.currentTimeMillis());
    	account.setDateCreated(currentDate);
    	account.setStatus("NotApproved");
    
    	Account savedAccount=accountRepository.save(account);
        User user = userRepository.findByUsername(account.getUsername());
        
        if (user != null) {
        	  if (user.getAadharNum() == null ) {
                  user.setAadharNum(aadhaarNumber);
              }
              if (user.getPanNum() == null) {
                  user.setPanNum(panNumber);
              }
              if (income!=null && user.getIncome() == null) {  
                  user.setIncome(income);
              }
            userRepository.save(user);
        }
        
        Date DoB=user.getDateOfBirth();
        int age = calculateAge(DoB, currentDate);
        if (age < 18) {
            throw new IllegalArgumentException("User is not eligible to open an account (under 18 years old).");
        }
        
        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setToWhom("BankManager");
        approvalRequest.setRequirement("AccountApproval"); 
        approvalRequest.setActionNeededOn(savedAccount.getAccountNumber());
        approvalRequest.setStatus("Pending"); 
        approvalRequest.setCreatedAt(currentDate);
        approvalRequestRepository.save(approvalRequest);
        
    	return savedAccount;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findById(accountNumber).orElse(null);
    }
    
    public List<Account> getAccountByNumberByusername(String username) {
        return accountRepository.findByUsername(username);
    }
    
    public String deleteAccount(String AccountNumber) {
    	if(accountRepository.existsById(AccountNumber)) {
    		accountRepository.deleteById(AccountNumber);
    		return "Account deleted successfully...";
    	}
		return "Account not deleted successfully";

    }


    private String generateAccountNo() {
        String maxAccountNo = accountRepository.findMaxAccountNumber();
         if (maxAccountNo == null) {
            return "1000";
        }
        int nextAccountNo = Integer.parseInt(maxAccountNo) + 1;
        return String.valueOf(nextAccountNo);
    }
	 
	 
	  public String generateAccountStatement(String accountNumber) {
	        StringBuilder statement = new StringBuilder();
	        
	        Optional<Account> accountOpt = accountRepository.findByAccountNumber(accountNumber);
	        if (accountOpt.isPresent()) 
	        {
	            Account account = accountOpt.get();
	            statement.append("Account Statement for Account Number: ").append(accountNumber).append("\n")
	                     .append("Account Type: ").append(account.getAccountType()).append("\n")
	                     .append("Balance: ").append(account.getBalance()).append("\n")
	                     .append("Branch Name: ").append(account.getBranchName()).append("\n")
	                     .append("IFSC Code: ").append(account.getIfscCode()).append("\n")
	                     .append("Status: ").append(account.getStatus()).append("\n\n");
	        } 
	        else 
	            return "Account not found!";
	        

	     List<Transaction> transactions = transRepository.findByAccountNumberOrderByTransactionDateDesc(accountNumber);
	        statement.append("Transaction History:\n");
	        for (Transaction transaction : transactions) 
	        {
	            statement.append("Date: ").append(transaction.getTransactionDate()).append(", ")
	                     .append("Type: ").append(transaction.getTransactionType()).append(", ")
	                     .append("Amount: ").append(transaction.getAmount()).append("\n");
	          }
	        
	        
	        return statement.toString();
	    }
	  
	public String generateFinancialReport(String accountNumber) {
	        StringBuilder report = new StringBuilder();

	        report.append("Financial report of an account with this accountnumber ").append(accountNumber).append(":\n\n");
        Double totalDeposits = transRepository.sumDeposits(accountNumber);
	        report.append("Total Deposits: ").append(totalDeposits != null ? totalDeposits : 0).append("\n");

	        Double totalWithdrawals = transRepository.sumWithdrawals(accountNumber);
	       report.append("Total Withdrawals: ").append(totalWithdrawals != null ? totalWithdrawals : 0).append("\n");

	     Optional<Account> accountOpt = accountRepository.findByAccountNumber(accountNumber);
	        if (accountOpt.isPresent()) 
	        {
	            report.append("Current Balance: ").append(accountOpt.get().getBalance()).append("\n");
	        } 
	        else 
	            report.append("Account not found!\n");
	        
	        return report.toString();
	    }
	
	private int calculateAge(Date dob, Date currentDate) {
	    Calendar birthCalendar = Calendar.getInstance();
	    birthCalendar.setTime(dob);
	    Calendar currentCalendar = Calendar.getInstance();
	    currentCalendar.setTime(currentDate);
	    int age = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

	    if (currentCalendar.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
	        age--;
	    }
	    return age;
	}
	



}
