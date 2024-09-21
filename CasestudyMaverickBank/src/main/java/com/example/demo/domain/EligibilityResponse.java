package com.example.demo.domain;

public class EligibilityResponse {

    private String accountNumber;
    private String username;
    private Double income;
    private Double inboundAmount;
    private Double outboundAmount;
    private String verdict;
    
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Double getIncome() {
		return income;
	}
	public void setIncome(Double income) {
		this.income = income;
	}
	public Double getInboundAmount() {
		return inboundAmount;
	}
	public void setInboundAmount(Double inboundAmount) {
		this.inboundAmount = inboundAmount;
	}
	public Double getOutboundAmount() {
		return outboundAmount;
	}
	public void setOutboundAmount(Double outboundAmount) {
		this.outboundAmount = outboundAmount;
	}
	public String getVerdict() {
		return verdict;
	}
	public void setVerdict(String verdict) {
		this.verdict = verdict;
	}

}