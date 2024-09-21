package com.example.demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "new_loanlist")
public class NewLoanList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_type_id")
    private Long loanTypeId;

    @Column(name = "loan_name", nullable = false)
    private String loanName;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "interest_rate", nullable = false)
    private Double interestRate;

    @Column(name = "period", nullable = false)
    private Integer period;
    
    @Column(name = "eligible_income", nullable = false)
    private Double eligibleIncome;
    
    @Column(name = "min_tenure_months", nullable = false)
    private int minTenure;

    
	public NewLoanList(Long loanTypeId, String loanName, Double amount, Double interestRate, Integer period,double eligibleIncome) {
		this.loanTypeId = loanTypeId;
		this.loanName = loanName;
		this.amount = amount;
		this.interestRate = interestRate;
		this.period = period;
		this.eligibleIncome=eligibleIncome;
	}
	
	

	public NewLoanList() {
		
	}



	public Long getLoanTypeId() {
		return loanTypeId;
	}

	public void setLoanTypeId(Long loanTypeId) {
		this.loanTypeId = loanTypeId;
	}

	public String getLoanName() {
		return loanName;
	}

	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Double getEligibleIncome() {
		return eligibleIncome;
	}



	public void setEligibleIncome(Double eligibleIncome) {
		this.eligibleIncome = eligibleIncome;
	}



	public int getMinTenure() {
		return minTenure;
	}



	public void setMinTenure(int minTenure) {
		this.minTenure = minTenure;
	}



	@Override
	public String toString() {
		return "NewLoanList [loanTypeId=" + loanTypeId + ", loanName=" + loanName + ", amount=" + amount
				+ ", interestRate=" + interestRate + ", period=" + period + ", eligibleIncome=" + eligibleIncome + "]";
	}




}