package com.example.demo.domain;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.domain.NewLoan;
import com.example.demo.domain.NewLoanList;
import com.example.demo.repository.NewLoanListRepository;
import com.example.demo.service.InstallmentsService;
import com.example.demo.service.NewLoanService;

@Entity
@Table(name = "installments")
public class Installments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "installments_id")
    private int installmentsId;

    @Column(name = "installment_amount", nullable = false)
    private double installmentAmount;

    @Column(name = "installment_status", nullable = false)
    private String installmentStatus;

    @Column(name ="installment_date",nullable=false)
    private Date installmentDate;
    
    @Column(name = "payment_date", nullable = false)
    private Date paymentDate;

    @Column(name = "payment_type", nullable = false)
    private String paymentType;

    @Column(name = "payment_amount", nullable = false)
    private double paymentAmount;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "loan_id", nullable = false)
    private int loanId;

    @ManyToOne
    @JoinColumn(name = "loan_id", referencedColumnName = "loan_id", insertable = false, updatable = false)
    private NewLoan newloan;

    
    public Installments() {
    	
    }
	public int getInstallmentsId() {
		return installmentsId;
	}

	public void setInstallmentsId(int installmentsId) {
		this.installmentsId = installmentsId;
	}

	public double getInstallmentAmount() {
		return installmentAmount;
	}

	public void setInstallmentAmount(double installmentAmount) {
		this.installmentAmount = installmentAmount;
	}

	public String getInstallmentStatus() {
		return installmentStatus;
	}

	public void setInstallmentStatus(String installmentStatus) {
		this.installmentStatus = installmentStatus;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getLoanId() {
		return loanId;
	}

	public void setLoanId(int loanId) {
		this.loanId = loanId;
	}

	public NewLoan getNewloan() {
		return newloan;
	}

	public void setNewloan(NewLoan newloan) {
		this.newloan = newloan;
	}
	public Date getInstallmentDate() {
		return installmentDate;
	}
	public void setInstallmentDate(Date installmentDate) {
		this.installmentDate = installmentDate;
	}
	
	

}
