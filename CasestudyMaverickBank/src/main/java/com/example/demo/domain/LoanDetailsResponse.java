package com.example.demo.domain;

public class LoanDetailsResponse {

	    private int loanId;
	    private String loanName;
	    private int repaymentPoints;
	    private int minTenureMonths;
	    private double outstandingBalance;
	    private String verdict;
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
		public int getRepaymentPoints() {
			return repaymentPoints;
		}
		public void setRepaymentPoints(int repaymentPoints) {
			this.repaymentPoints = repaymentPoints;
		}
		public int getMinTenureMonths() {
			return minTenureMonths;
		}
		public void setMinTenureMonths(int minTenureMonths) {
			this.minTenureMonths = minTenureMonths;
		}
		public double getOutstandingBalance() {
			return outstandingBalance;
		}
		public void setOutstandingBalance(double outstandingBalance) {
			this.outstandingBalance = outstandingBalance;
		}
		public String getVerdict() {
			return verdict;
		}
		public void setVerdict(String verdict) {
			this.verdict = verdict;
		}
		
		public LoanDetailsResponse(int loanId, String loanName, int repaymentPoints, int minTenureMonths,
				double outstandingBalance, String verdict) {
			this.loanId = loanId;
			this.loanName = loanName;
			this.repaymentPoints = repaymentPoints;
			this.minTenureMonths = minTenureMonths;
			this.outstandingBalance = outstandingBalance;
			this.verdict = verdict;
		}
		public LoanDetailsResponse() {
			
		}
		
	    
		
}
