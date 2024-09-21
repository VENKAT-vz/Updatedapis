package com.example.demo.domain;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "new_loan")
public class NewLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private int loanId;

    @Column(name = "loan_name", nullable = false)
    private String loanName;

    @Column(name = "loan_amount", nullable = false)
    private double loanAmount;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "sanction_amount", nullable = false)
    private double sanctionAmount;

    @Column(name = "period", nullable = false)
    private int period;

    @Column(name = "installments_per_month", nullable = false)
    private double installmentsPerMonth;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @Column(name = "loan_approved_date")
    private Date loanApprovedDate;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;
    
    @Column(name="repayment_points", nullable=true)
    private int repaymentPoints;
    
    @ManyToOne
    @JoinColumn(name = "account_number", referencedColumnName = "account_number", insertable = false, updatable = false)
    private Account account;

	public int getLoanId() {
		return loanId;
	}

	public void setLoanId(int loanId) {
		this.loanId = loanId;
	}

	public String getLoanName() {
		return loanName;
	}

	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}

	public double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getSanctionAmount() {
		return sanctionAmount;
	}

	public void setSanctionAmount(double sanctionAmount) {
		this.sanctionAmount = sanctionAmount;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public double getInstallmentsPerMonth() {
		return installmentsPerMonth;
	}

	public void setInstallmentsPerMonth(double installmentsPerMonth2) {
		this.installmentsPerMonth = installmentsPerMonth2;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Date getLoanApprovedDate() {
		return loanApprovedDate;
	}

	public void setLoanApprovedDate(Date loanApprovedDate) {
		this.loanApprovedDate = loanApprovedDate;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public int getRepaymentPoints() {
		return repaymentPoints;
	}

	public void setRepaymentPoints(int repaymentPoints) {
		this.repaymentPoints = repaymentPoints;
	}

}