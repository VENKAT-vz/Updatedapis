package com.example.demo.domain;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Date;

@Entity
@Table(name = "approval_requests")
public class ApprovalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private int requestId;

    @JsonIgnore
    @Column(name = "to_whom", nullable = false)
    private String toWhom; // 'Admin' or 'BankManager'

    @Column(name = "requirement", nullable = false)
    private String requirement; // 'LoanClosure', 'AccountClosure', 'UserAccountClosure'

    @Column(name = "action_needed_on", nullable = false)
    private String actionNeededOn; // Loan ID, Account Number, or Username

    @Column(name = "status", nullable = false)
    private String status; // 'Approved' or 'Rejected'

    @Column(name = "created_at", updatable = false)
    private Date createdAt;
    
    @Column(name = "remarks", nullable=true)
    private String remarks;

    // Constructors
    public ApprovalRequest() {}

    public ApprovalRequest(String toWhom, String requirement, String actionNeededOn, String status, String remarks) {
        this.toWhom = toWhom;
        this.requirement = requirement;
        this.actionNeededOn = actionNeededOn;
        this.status = status;
        this.createdAt = new Date(System.currentTimeMillis()); // Set current date
        this.remarks=remarks;
    }


    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getToWhom() {
        return toWhom;
    }

    public void setToWhom(String toWhom) {
        this.toWhom = toWhom;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getActionNeededOn() {
        return actionNeededOn;
    }

    public void setActionNeededOn(String actionNeededOn) {
        this.actionNeededOn = actionNeededOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
