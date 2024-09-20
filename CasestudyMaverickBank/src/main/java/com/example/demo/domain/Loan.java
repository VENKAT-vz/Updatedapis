package com.example.demo.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="loans")
public class Loan {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int loanId;
	@Column(name="account_number",nullable = false)
	private String accountNumber;
	@Column(name="loan_type")
    private String loanType; 
	@Column(name="amount")
    private double amount; 
	@Column(name="purpose")
    private String purpose;
	@Column(name="loan_created_date")
	private Date dateCreated;
	@Column(name="status")
    private String status;
	
	@ManyToOne
    @JoinColumn(name = "account_number", referencedColumnName = "account_number", insertable = false, updatable = false)
    private Account account;

	
	public Loan(int loanId, String accountNumber, String loanType, double amount, String purpose,Date dateCreated,
			String status, Account account) {
		this.loanId = loanId;
		this.accountNumber = accountNumber;
		this.loanType = loanType;
		this.amount = amount;
		this.purpose = purpose;
		this.dateCreated = dateCreated;
		this.status = status;
		this.account = account;
	}

	
	public Loan() {
		
	}


	public int getLoanId() {
		return loanId;
	}

	public void setLoanId(int loanId) {
		this.loanId = loanId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "Loan [loanId=" + loanId + ", accountNumber=" + accountNumber + ", loanType=" + loanType + ", amount="
				+ amount + ", dateCreated=" + dateCreated + ", purpose=" + purpose + ", status=" + status + ", account="
				+ account + "]";
	}
	
	
	
	
}
