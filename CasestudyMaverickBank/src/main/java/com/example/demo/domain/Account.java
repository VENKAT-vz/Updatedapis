package com.example.demo.domain;


import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "account_type", nullable = true)
    private String accountType;

    @Column(name = "balance", nullable = false)
    private double balance;

    @Column(name = "branch_name", nullable = true)
    private String branchName;

    @Column(name = "ifsc_code", nullable = true)
    private String ifscCode;

    @Column(name = "date_created", nullable = true)
    private Date dateCreated;

    @Column(name = "status", nullable = true)
    private String status;

    @Column(name = "username")
    private String username;

    @Column(name = "emailid")
    private String emailid;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false),
        @JoinColumn(name = "emailid", referencedColumnName = "emailid", insertable = false, updatable = false)
    })
    private User user;


    public Account() {
    	
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

	@Override
	public String toString() {
		return "Account [accountNumber=" + accountNumber + ", accountType=" + accountType + ", balance=" + balance
				+ ", branchName=" + branchName + ", ifscCode=" + ifscCode + ", dateCreated=" + dateCreated + ", status="
				+ status + ", username=" + username + ", emailid=" + emailid + ", user=" + user + "]";
	}

    
}
